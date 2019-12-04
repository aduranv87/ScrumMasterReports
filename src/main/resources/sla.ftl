<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

    <title>Email for Virtual Scrum Master</title>

    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <style>
        body{
            font-family: Verdana;
        }

        td {
            padding: 5px;
        }

        .box {
            padding: 1px 0px 1px 0px;
            border-radius: 10px 10px 10px 10px;
            text-align: center;
            width: 200px;
            color:white;
            margin-bottom: 10px;
        }

        .green {
            background: #32a852;
        }

        .orange {
            background: #ffaa42;
        }

        .red {
            background: red;
        }
    </style>
</head>
<body style="margin: 10px; padding: 0;">
<p>&nbsp;</p>
<div style="float: left; margin-right: 45px; width: 30%;">
    <div class="green box"><h2>Going good</h2></div>
    <table border="1" cellpadding="0" cellspacing="0" width="100%">
        <tr>
            <td>Issue</td>
        </tr>
        <#list greenIssues as gi>
            <tr>
                <td> <a target="_blank" href="https://sevensenders.atlassian.net/browse/${gi.key}">${gi.key}</a></td>
            </tr>
        </#list>
    </table>
</div>
<div style="float:left; width: 46%">
    <div class="orange box"><h2>SLA Breach</h2></div>
    <table border="1" cellpadding="0" cellspacing="0" width="100%">
        <tr>
            <td>Issue</td><td>Sprint Points</td><td>Reason</td>
        </tr>
        <#list orangeIssues as oi>
            <tr>
                <td> <a target="_blank" href="https://sevensenders.atlassian.net/browse/${oi.key}">${oi.key}</a></td> <td> ${oi.points}</td> <td> ${oi.reason}</td>
            </tr>
        </#list>
    </table>
</div>
<p style="clear: both;">&nbsp;</p>
<div>
    <div class="red box"><h2>Sprint goals</h2></div>
    <table border="1" cellpadding="0" cellspacing="0" width="80%">
        <tr>
            <td>Issue</td><td>Sprint Points</td><td>Reason</td>
        </tr>

        <#list redIssues as ri>
            <tr>
                <td> <a target="_blank" href="https://sevensenders.atlassian.net/browse/${ri.key}">${ri.key}</a></td> <td> ${ri.points}</td> <td> ${ri.reason}</td>
            </tr>
        </#list>
    </table>
    </div>
</div>
</body>
</html>
