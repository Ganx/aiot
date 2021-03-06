package cn.neyzoter.aiot.dal.domain.vehicle;

import java.io.Serializable;
import java.util.*;

/**
 * Vehicle class
 * @author Neyzoter Song
 * @date 2019/9/7
 */
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 3519879653978313723L;
    private Long app;
    private String vid;
    private String vtype;
    /**
     * runtime data map, key is millisecond , value is RuntimeData
     */
    private SortedMap<Long, RuntimeData> rtDataMap;

    /**
     * get Application id
     * @return
     */
    public Long getApp() {
        return this.app;
    }

    /**
     * set appliacction id
     * @param application application
     */
    public void setApp(Long application) {
        this.app = application;
    }


    /**
     * get rt data map
     * @return
     */
    public SortedMap<Long, RuntimeData> getRtDataMap () {
        return this.rtDataMap;
    }

    /**
     * set rt data map
     * @param data rt data
     */
    public void setRtDataMap (SortedMap<Long, RuntimeData> data) {
        this.rtDataMap = data;
    }

    /**
     * get vehicle id
     * @return vid
     */
    public String getVid () {
        return this.vid;
    }

    /**
     * set vehicle id
     * @param id vehicle id
     */
    public void setVid (String id) {
        this.vid = id;
    }

    /**
     * get vehicle type
     * @return
     */
    public String getVtype () {
        return this.vtype;
    }

    /**
     * set vehicle type
     * @param type vehicle type
     */
    public void setVtype (String type) {
        this.vtype = type;
    }

    @Override
    public String toString(){
        String str = new String();
        try {
            str += "{" +
                    this.toTags() + "," +
                    "rtDataMap={";
            Iterator it = rtDataMap.entrySet().iterator();
            for (;it.hasNext();) {
                Map.Entry data = (Map.Entry)it.next();
                str += String.format("%d=%s%s",data.getKey(),data.getValue().toString(),it.hasNext()?",":"");
            }
            str += "}";
        }catch (Exception e) {
            System.err.println(e);
        }
        return str;
    }

    /**
     * transform to 2-array<br/>
     * [time][var num]
     * @return Double[][]
     */
    public Double[][] toArray () {
        Iterator<Map.Entry<Long, RuntimeData>> iter = rtDataMap.entrySet().iterator();
        int rtDataNum = rtDataMap.size();
        Double[][] array = new Double[rtDataNum][RuntimeData.VAR_NUM];
        for (int time = 0;time < rtDataNum; time ++) {
            Map.Entry<Long, RuntimeData> entry = iter.next();
            try {
                Double[] arr = entry.getValue().toArray();
                System.arraycopy(arr, 0, array[time], 0, RuntimeData.VAR_NUM);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        return array;
    }

    /**
     * transform to 2-array<br/>
     * [var num][time]
     * @return Double[][]
     */
    public Double[][] toArrayT () {
        Double[][] array = toArray();
        int timeNum = array.length;
        int varNum = array[0].length;
        Double[][] arrayT = new Double[varNum][timeNum];
        for (int i = 0; i < timeNum; i ++) {
            for (int j = 0; j < varNum; j ++) {
                arrayT[j][i] = array[i][j];
            }
        }
        return arrayT;
    }

    /**
     * get tags, compatible to influx
     * @return tags
     */
    public String toTags () {
        String tags = "app="+this.app + ","+
                "vid=" + this.vid + ","+
                "vtype=" + this.vtype;
        return tags;
    }
    /**
     * to tags fields timestamp , compatible to influx's "tag[,tag...] field[,field...] timestamp" <br></>
     * note that tag and field is "k=v", but timestamp is just a time num.
     * @return List<String>
     */
    public List<String> toTagsFieldsTimestamp () {
        List<String> influxStrs = new ArrayList<>();
        String influxStr;
        Iterator<Map.Entry<Long, RuntimeData>> iter = rtDataMap.entrySet().iterator();
        for (;iter.hasNext();) {
            Map.Entry<Long, RuntimeData> entry = iter.next();
            // "tag[,tag...] field[,field...] timestamp"
            influxStr = this.toTags() + " " + entry.getValue().toFields() + " " + entry.getKey();
            influxStrs.add(influxStr);
        }
        return influxStrs;
    }
}
