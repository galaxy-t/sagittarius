package com.galaxyt.sagittarius.client.utils.http;

import com.galaxyt.sagittarius.common.exception.SagittariusException;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * HTTP 长链接工具类
 * @author zhouqi
 * @date 2019-07-05 18:17
 * @version v1.0.0
 * @Description
 *
 *  什么是HTTP持久连接
 *    HTTP持久连接（也称为HTTP保持活动或HTTP连接重用）是使用相同的TCP连接发送和接收多个HTTP请求/响应的想法，
 *    而不是为每个请求/响应对打开一个新的。使用持久连接对于提高HTTP性能非常重要。
 *
 *  优点：
 *    1. 网络友好。由于较少的TCP连接设置和拆除而减少了网络流量。
 *    2. 减少后续请求的延迟。由于避免了初始TCP握手
 *    3. 持久的连接允许TCP有足够的时间来确定网络的拥塞状态，从而做出适当的反应。
 *
 *    使用HTTPS或HTTP over SSL / TLS，优势更加明显。
 *    在那里，除了初始TCP连接设置之外，持久连接可以减少建立安全关联的昂贵SSL / TLS握手的数量。
 *
 *    在HTTP / 1.1中，持久连接是任何连接的默认行为。
 *    也就是说，除非另有说明，客户端应该假设服务器将保持持久连接，即使在服务器的错误响应之后也是如此。
 *    但是，该协议为客户端和服务器提供了发出TCP连接关闭信号的方法。
 *
 *    参考：https://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-05 18:17     zhouqi          v1.0.0           Created
 *
 */
public class HttpLongPollUtil {




  /**
   * 单次请求超时时间
   * 90秒，应该比服务器端的长轮询超时长
   */
  private static final int LONG_POLLING_READ_TIMEOUT = 90 * 1000;

  /**
   * 默认连接超时时间
   */
  private static int CONNECT_TIMEOUT = 1000; //1 秒


  /**
   * 已长链接的方式执行 GET 请求
   * @param url 要请求的 URL
   * @return  Response 中的 body，可能为 NULL 也可能为 空字符串，若服务端未填充响应 BODY 则为空字符串
   */
  public Response doGet(String url) {

    InputStreamReader isr = null;
    InputStreamReader esr = null;
    int responseCode;
    try {

      HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
      conn.setRequestMethod("GET");

      conn.setReadTimeout(LONG_POLLING_READ_TIMEOUT);
      conn.setConnectTimeout(CONNECT_TIMEOUT);

      conn.connect();

      responseCode = conn.getResponseCode();
      String responseBody = null;

      try {
        isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
        responseBody = CharStreams.toString(isr);
      } catch (IOException ex) {
        /**
         * 我们应该通过读取响应主体来清理连接，以便可以重用连接。
         * 在其出现异常的情况下，将其全部的响应信息读出来，清空它，让其达到空闲状态会自动被缓存
         * 不出现异常的情况当然不用怀疑，响应信息会被完整的消费完
         * 但是异常情况如果不消费流中的异常信息则会导致这个连接无法被清理重用（考虑一下，一个连接是一个整体，
         * 这个整体如果想被重用则需要恢复到原始状态，可现在这个整体的某一部分，
         * 也就是这个异常响应流还有内容，而且底层也不会帮你清理这些流，因为它也不知道这些内容对你是否有用，
         * 可能你真的对这些异常信息不感兴趣，但这正是导致这个连接无法被重用的关键，它只会因为长时间不用而被 gc 清理掉）
         */
        InputStream errorStream = conn.getErrorStream();

        if (errorStream != null) {
          esr = new InputStreamReader(errorStream, StandardCharsets.UTF_8);
          try {
            CharStreams.toString(esr);
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        }

        // 200 和 304 时不应该触发 IOException，所以应该将原始异常抛出
        // 若响应码为 200 或 304 时，代表已经为正常的响应，但可能在读取流或者转换流的时候出现的异常，虽然可能性很小
        // 所以我们将原始异常抛出，以便调用方进行捕捉或者处理
        if (responseCode == 200 || responseCode == 304) {
          throw ex;
        } else {
          //像 404 这样的状态码，在调用 conn.getInputStream() 时需要 IOException
          throw new SagittariusException(String.format("Sagittarius 请求服务端时出现异常，ResponseCode：%s，URL：%s", responseCode, url), ex);
        }

      }

      /*
      响应码为 200 ，表示本次请求成功
      或许不一定有 responseBody ，但确实是一个正常的响应
       */
      if (responseCode == 200) {
        return new Response(responseCode, responseBody);
      }

      /*
      长链接超时，返回状态吗和 NULL 的body即可
       */
      if (responseCode == 304) {
        return new Response(responseCode, null);
      }
    } catch (SagittariusException ex) {
      throw ex;
    } catch (Throwable ex) {
      throw new SagittariusException(String.format("Sagittarius 请求服务端时出现异常，URL：%s", url), ex);
    } finally {
      if (isr != null) {
        try {
          isr.close();
        } catch (IOException ex) {
        }
      }

      if (esr != null) {
        try {
          esr.close();
        } catch (IOException ex) {
        }
      }
    }

    throw new SagittariusException(String.format("Sagittarius 请求服务端时出现异常，ResponseCode：%s，URL：%s", responseCode, url));


  }

}
