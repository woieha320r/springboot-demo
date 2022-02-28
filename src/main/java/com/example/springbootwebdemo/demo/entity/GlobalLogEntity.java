package com.example.springbootwebdemo.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.springbootwebdemo.core.log.GlobalLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 全局日志
 * </p>
 *
  * @since 2022-01-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("global_log")
@ApiModel(value = "GlobalLogEntity对象", description = "全局日志")
public class GlobalLogEntity implements Serializable, GlobalLog {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键。自增")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("HTTP方法")
    @TableField("http_method")
    private String httpMethod;

    @ApiModelProperty("登录账户")
    @TableField("login_account")
    private Long loginAccount;

    @ApiModelProperty("方法描述")
    @TableField("method_description")
    private String methodDescription;

    @ApiModelProperty("请求路径")
    @TableField("request_url")
    private String requestUrl;

    @ApiModelProperty("客户端IP")
    @TableField("remote_address")
    private String remoteAddress;

    @ApiModelProperty("客户端主机名")
    @TableField("remote_host")
    private String remoteHost;

    @ApiModelProperty("客户端端口")
    @TableField("remote_port")
    private Integer remotePort;

    @ApiModelProperty("接收请求的服务端IP")
    @TableField("local_address")
    private String localAddress;

    @ApiModelProperty("接收请求的服务端主机名")
    @TableField("local_name")
    private String localName;

    @ApiModelProperty("接收请求的服务端端口")
    @TableField("local_port")
    private Integer localPort;

    @ApiModelProperty("请求时间")
    @TableField("request_time")
    private LocalDateTime requestTime;

    @ApiModelProperty("请求是否成功")
    @TableField("is_success")
    private Boolean success;

    @ApiModelProperty("执行的方法路径")
    @TableField("class_path")
    private String classPath;

    @ApiModelProperty("请求参数")
    @TableField("request_param")
    private String requestParam;

    @ApiModelProperty("响应体")
    @TableField("response_body")
    private String responseBody;

    @ApiModelProperty("异常信息")
    @TableField("exception_message")
    private String exceptionMessage;

    @ApiModelProperty("乐观锁")
    @TableField(value = "version", fill = FieldFill.INSERT)
    @Version
    private Integer version;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty("创建时间")
    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("修改时间")
    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

}
