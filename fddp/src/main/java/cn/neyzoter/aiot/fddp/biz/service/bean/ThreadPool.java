package cn.neyzoter.aiot.fddp.biz.service.bean;

import cn.neyzoter.aiot.fddp.biz.service.properties.PropertiesManager;
import cn.neyzoter.aiot.fddp.biz.service.tensorflow.TfModelAliveChecker;
import cn.neyzoter.aiot.fddp.biz.service.tensorflow.VehicleModelTable;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * my thread pool cooker
 * @author Neyzoter Song
 * @date 2020-3-7
 */
@ComponentScan("cn.neyzoter.aiot.fddp.biz.service.properties,cn.neyzoter.aiot.fddp.biz.service.tensorflow")
@Component
public class ThreadPool {
    public static final Logger logger = LoggerFactory.getLogger(ThreadPool.class);
    public static ScheduledExecutorService scheduledThreadPool;

    private PropertiesManager propertiesUtil;
    @Autowired
    ThreadPool (PropertiesManager propertiesUtil, TfModelAliveChecker tfModelAliveChecker) {
        this.propertiesUtil = propertiesUtil;
        scheduledThreadPool = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("scheduled-pool-%d").daemon(true).build());
        // alive tensorflow model check
        VehicleModelTable table = tfModelAliveChecker.getVehicleModelTable();
        table.put("mazida1020", propertiesUtil.readValue(PropertiesLables.TENSORFLOW_MODEL_PATH),
                "mytag", table.getMaxAliveTime());
        scheduledThreadPool.scheduleAtFixedRate(tfModelAliveChecker,0,
                tfModelAliveChecker.getVehicleModelTable().getPeriod(),tfModelAliveChecker.getVehicleModelTable().getTimeUnit());
        logger.info(String.format("TF Model Checker period : %d, Unit : %s", tfModelAliveChecker.getVehicleModelTable().getPeriod(),tfModelAliveChecker.getVehicleModelTable().getTimeUnit().toString() ));
        // properteis update
        scheduledThreadPool.scheduleAtFixedRate(this.propertiesUtil, 0,
                propertiesUtil.getPeriod(),
                propertiesUtil.getUpdatePeriodUnit());
        logger.info(String.format("Properties Update period : %d, Unit : %s", propertiesUtil.getPeriod(), propertiesUtil.getUpdatePeriodUnit() ));
    }


}