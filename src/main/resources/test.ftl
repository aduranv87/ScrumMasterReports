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

        .numberCircle {
            border-radius: 50%;
            width: 72px;
            height: 72px;
            padding: 16px;

            background: #fff;
            border: 4px solid #666;
            color: #666;
            text-align: center;

            font: 64px Arial, sans-serif;
        }
    </style>
</head>
<body style="margin: 10px; padding: 0;">

<table style="width:700px;" align="center">
    <tr><td align="center"><strong>Perfect Score</strong></td><td><strong>Reasons</strong></td></tr>
    <tr><td align="center"><div class="numberCircle">32</div></td><td><p>1. Total: 36</p>
            <p>2. Real capacity: 27</p>
            <p>3. Carry on points: ${carryon}</p>
            <p>4. Estimated points: ${estimated}</p>
        </td></tr>
</table>

<table border="1" cellpadding="0" cellspacing="0" width="80%">
    <tr>
        <td>Sprint</td><td>Estimated</td><td>Completed</td>
    </tr>

    <#list sprints as sprint>
        <tr>
            <td> ${sprint.name}</td> <td> ${sprint.estimated}</td> <td>${sprint.completed}</td>
        </tr>
    </#list>

</table>
</body>
</html>
