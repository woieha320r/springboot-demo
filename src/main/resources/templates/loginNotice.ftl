<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${subject}</title>
</head>
<body>
<pre>
<#--${nickname}，您好-->
<#if nickname??>${nickname}，<#else></#if>，您好
    这是一封登录提醒邮件，您的账户于${loginTime.format('yyyy-MM-dd HH:mm:ss')}登录本站。
</pre>
</body>
</html>