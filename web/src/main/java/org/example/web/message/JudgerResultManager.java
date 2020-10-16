package org.example.web.message;

import org.example.bean.JudgerResult;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Cnlomou
 * @create 2020/8/12 19:51
 */
@Component
public class JudgerResultManager {

    private Map<Integer, Data> map=new ConcurrentHashMap<>();

    public void add(int subId,JudgerResult judgerResult){
        if(map.containsKey(subId)){
            Data data = map.get(subId);
            data.judgerResult=judgerResult;
            data.thread.interrupt();//唤醒
        }else {
            Data data = new Data();
            data.judgerResult=judgerResult;
            map.put(subId,data);
        }
    }

    public void del(int subId){
        map.remove(subId);
    }

    public JudgerResult getResult(int subId){
        return map.get(subId).judgerResult;
    }

    public void wait(int subId){
        if(!map.containsKey(subId)){
            try {
                Data data = new Data();
                data.thread=Thread.currentThread();
                map.put(subId,data);
                Thread.sleep(15*1000);
            } catch (InterruptedException e) {
            }
        }
    }
    class Data{
        private Thread thread;
        private JudgerResult judgerResult;
    }
}
