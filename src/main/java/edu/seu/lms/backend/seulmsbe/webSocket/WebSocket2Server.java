package edu.seu.lms.backend.seulmsbe.webSocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import edu.seu.lms.backend.seulmsbe.Student_Curriculum.mapper.StudentCurriculumMapper;
import edu.seu.lms.backend.seulmsbe.checkin.mapper.CheckinMapper;
import edu.seu.lms.backend.seulmsbe.dto.WebSocketDTO;
import edu.seu.lms.backend.seulmsbe.dto.checkInData;
import edu.seu.lms.backend.seulmsbe.syllabus.entity.Syllabus;
import edu.seu.lms.backend.seulmsbe.syllabus.mapper.SyllabusMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@ServerEndpoint("/ws/refreshCheckIn/{userid}")
@Component
public class WebSocket2Server {

    private static SyllabusMapper syllabusMapper;
    @Autowired
    public void setSyllabusMapper(SyllabusMapper syllabusMapperTmp) {
        syllabusMapper=syllabusMapperTmp;
    }
    private static CheckinMapper checkinMapper;
    @Autowired
    public void setCheckinMapper(CheckinMapper checkinMapperTmp) {
        checkinMapper = checkinMapperTmp;
    }
    private static StudentCurriculumMapper studentCurriculumMapper;
    @Autowired
    public void setStudentCurriculumMapper(StudentCurriculumMapper studentCurriculumMappertmp) {
        studentCurriculumMapper = studentCurriculumMappertmp;
    }
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的
     */
    private static int onlineCount = 0;
    //public static WebSocket2Server test2;
    /**
     * concurrent 包的线程安全Set，用来存放每个客户端对应的 myWebSocket对象
     * 根据userId来获取对应的 WebSocket
     */
    public static ConcurrentHashMap<String, WebSocket2Server> webSocketMap = new ConcurrentHashMap<String, WebSocket2Server>();
    //public static List<WebSocket2Server> list=new ArrayList<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    public Session session;

    //public Integer num;
    /**
     * 接收 sid
     */
    private String userid = "";


    /**
     * 连接建立成功调用的方法
     * 创建连接后自动将当前连接加入静态Map中
     * @param session
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("userid") String userid) {
        this.session = session;

        this.userid = userid;
//
        webSocketMap.put(userid, this);
//        log.info("webSocketMap -> " + JSON.toJSONString(webSocketMap));
//
        addOnlineCount(); // 在线数 +1
//        log.info("有新窗口开始监听:" + userId + ",当前在线人数为" + getOnlineCount());
        System.out.println("successssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        try {

            sendMessage(JSON.toJSONString("连接成功"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException("websocket IO异常！！！！");
        }

    }
//    @OnMessage
//    public void onMessage(String syllabusID){
//        Syllabus syllabus = syllabusMapper.selectById(syllabusID);
//        if(syllabus.getIsCheckedIn()==0){
//            WebSocketDTO webSocketDTO = new WebSocketDTO();
//            //Syllabus syllabus = syllabusMapper.selectById(syllabusID);
//            webSocketDTO.setPassword(syllabus.getCheckInPsw()==null?"":syllabus.getCheckInPsw());
//            checkInData checkInData = new checkInData();
//            checkInData.setIsCheckedIn(0);
//            checkInData.setNotCheckedIn(studentCurriculumMapper.getNumofCourse(syllabus.getCurriculumID()));
//            webSocketDTO.setCheckInData(checkInData);
//            try {
//                sendMessage(JSONObject.toJSONString(webSocketDTO));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }else{
//            WebSocketDTO webSocketDTO = new WebSocketDTO();
//            //Syllabus syllabus = syllabusMapper.selectById(syllabusID);
//            webSocketDTO.setPassword(syllabus.getCheckInPsw()==null?"":syllabus.getCheckInPsw());
//            checkInData checkInData = new checkInData();
//            checkInData.setIsCheckedIn(checkinMapper.getCheckedNum(syllabusID));
//            checkInData.setNotCheckedIn(checkinMapper.getNotCheckedNum(syllabusID));
//            webSocketDTO.setCheckInData(checkInData);
//            try {
//                sendMessage(JSONObject.toJSONString(webSocketDTO));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//    }

    /**
     * 关闭连接
     * 关闭连接自动将Map中记录的连接清除
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.get(this.userid) != null) {
            webSocketMap.remove(this.userid);
            subOnlineCount(); // 人数 -1
            log.info("有一连接关闭，当前在线人数为：" + getOnlineCount());
        }
        try {
            sendMessage(JSON.toJSONString("连接断开"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException("websocket IO异常！！！！");
        }

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session
     */
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        log.info("收到来自窗口" + userId + "的信息：" + message);
//
//        if (StringUtils.isNotBlank(message)) {
//            try {
//                // 解析发送的报文
//                JSONObject jsonObject = JSON.parseObject(message);
//                // 追加发送人（防窜改）
//                jsonObject.put("fromUserId", this.userId);
//                String toUserId = jsonObject.getString("toUserId");
//                // 传送给对应 toUserId 用户的 WebSocket
//                if (StringUtils.isNotBlank(toUserId) && webSocketMap.containsKey(toUserId)) {
//                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
//                } else {
//                    log.info("请求的userId：" + toUserId + "不在该服务器上"); // 否则不在这个服务器上，发送到 MySQL 或者 Redis
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     *
     * @param message
     * @throws IOException
     */
    public static void sendInfo(String message) throws IOException {

        // 遍历集合，可设置为推送给指定sid，为 null 时发送给所有人
        Iterator entrys = webSocketMap.entrySet().iterator();
        while (entrys.hasNext()) {
            Map.Entry entry = (Map.Entry) entrys.next();
           // if (userId == null) {
            webSocketMap.get(entry.getKey()).sendMessage(message);
                //log.info("发送消息到：" + entry.getKey() + "，消息：" + message);
          //  } else if (entry.getKey().equals(userId)) {
            //    webSocketMap.get(entry.getKey()).sendMessage(message);
                //log.info("发送消息到：" + entry.getKey() + "，消息：" + message);
           // }

        }
    }
    public void sendCheckIn(WebSocketDTO webSocketDTO) throws IOException, EncodeException {
        this.session.getBasicRemote().sendObject(webSocketDTO);
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocket2Server.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocket2Server.onlineCount--;
    }
}

