package edu.seu.lms.backend.seulmsbe.webSocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
@ServerEndpoint("/ws/realTimeCheckIn")
@Component
public class WebSocketServer {
//    @Autowired
//    private CheckinMapper checkinMapper;
//
//    @Autowired
//    private SyllabusMapper syllabusMapper;

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

    public static WebSocketServer test;//记录静态量向该端发送消息


    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    public Session session;
    //public Integer num;



    /**
     * 连接建立成功调用的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("syllabusID") String syllabusID) {
        this.session = session;
        test = this;
       // num++;
//        this.session = session;
//        this.userId = userId;
//
//        webSocketMap.put(userId, this);
//        log.info("webSocketMap -> " + JSON.toJSONString(webSocketMap));
//
//        addOnlineCount(); // 在线数 +1
//        log.info("有新窗口开始监听:" + userId + ",当前在线人数为" + getOnlineCount());
        System.out.println("success");
        try {

            sendMessage(JSON.toJSONString("连接成功"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException("websocket IO异常！！！！");
        }

    }
    @OnMessage
    public void onMessage(String syllabusID){ //收到消息自动返回所需数据
        Syllabus syllabus = syllabusMapper.selectById(syllabusID);
        if(syllabus.getIsCheckedIn()==0){
            WebSocketDTO webSocketDTO = new WebSocketDTO();
            //Syllabus syllabus = syllabusMapper.selectById(syllabusID);
            webSocketDTO.setPassword(syllabus.getCheckInPsw()==null?"":syllabus.getCheckInPsw());
            checkInData checkInData = new checkInData();
            checkInData.setIsCheckedIn(0);
            checkInData.setNotCheckedIn(studentCurriculumMapper.getNumofCourse(syllabus.getCurriculumID()));
            webSocketDTO.setCheckInData(checkInData);
            try {
                sendMessage(JSONObject.toJSONString(webSocketDTO));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            WebSocketDTO webSocketDTO = new WebSocketDTO();
            //Syllabus syllabus = syllabusMapper.selectById(syllabusID);
            webSocketDTO.setPassword(syllabus.getCheckInPsw()==null?"":syllabus.getCheckInPsw());
            checkInData checkInData = new checkInData();
            checkInData.setIsCheckedIn(checkinMapper.getCheckedNum(syllabusID));
            checkInData.setNotCheckedIn(checkinMapper.getNotCheckedNum(syllabusID));
            webSocketDTO.setCheckInData(checkInData);
            try {
                sendMessage(JSONObject.toJSONString(webSocketDTO));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 关闭连接
     */

//    @OnClose
//    public void onClose() {
////        if (webSocketMap.get(this.userId) != null) {
////            webSocketMap.remove(this.userId);
////            subOnlineCount(); // 人数 -1
////            log.info("有一连接关闭，当前在线人数为：" + getOnlineCount());
////        }
//        try {
//            sendMessage(JSON.toJSONString("连接断开"));
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new ApiException("websocket IO异常！！！！");
//        }
//
//    }

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

    public void sendCheckIn(WebSocketDTO webSocketDTO) throws IOException, EncodeException {
        this.session.getBasicRemote().sendObject(webSocketDTO);
    }

}

