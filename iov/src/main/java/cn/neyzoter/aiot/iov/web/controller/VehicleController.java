package cn.neyzoter.aiot.iov.web.controller;

import cn.neyzoter.aiot.dal.pojo.Vehicle2InfluxDb;
import cn.neyzoter.aiot.iov.biz.domain.vehicle.VehicleHttpPack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;



/**
 * Vehicle-relative controller
 * @author Neyzoter Song
 * @date 2019/9/7
 */
@RestController
public class VehicleController {
    private final String apiPrefix = "/iov/api/vehicle";
    @Autowired
    private KafkaTemplate kafkaTemplate;


    /**
     * http test( brower visit http://localhost:[port]/iov/api/vehicle/test)
     * @return {@link String}
     */
    @RequestMapping(value = apiPrefix+"/test", method = RequestMethod.GET)
    public String test() {
        return "OK";
    }


    /**
     * server get vehicle data
     * @param @RequestBody VehicleHttpPack
     * @return {@link String}
     */
    @RequestMapping(value = apiPrefix+"/sendData", method = RequestMethod.POST)
    public Object sendData(@RequestBody VehicleHttpPack vehicleHttpPack) {  //convert serialization


        // send to kafka-VehicleHttpPack
        kafkaTemplate.send("VehicleHttpPack", vehicleHttpPack);

        return "OK";
    }

}