<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 11-5-18
  Time: 下午5:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Admin Template</title>
<link href="styles/layout.css" rel="stylesheet" type="text/css" />
<link href="styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="themes/blue/styles.css" rel="stylesheet" type="text/css" />
<!-- Theme End -->
</head>
<body id="homepage">
	<div id="header">
    	<a href="" title=""><img src="img/cp_logo.png" alt="Control Panel" class="logo" /></a>
    	<div id="searcharea">
            <p class="left smltxt"><a href="#" title="">Advanced</a></p>
            <input type="text" class="searchbox" value="Search control panel..." onclick="if (this.value =='Search control panel...'){this.value=''}"/>
            <input type="submit" value="Search" class="searchbtn" />
        </div>
    </div>

    <!-- Top Breadcrumb Start -->
    <div id="breadcrumb">
    	<ul>
        	<li><img src="img/icons/icon_breadcrumb.png" alt="Location" /></li>
        	<li><strong>Location:</strong></li>
            <li><a href="#" title="">Sub Section</a></li>
            <li>/</li>
            <li class="current">Control Panel</li>
        </ul>
    </div>
    <!-- Top Breadcrumb End -->

    <!-- Right Side/Main Content Start -->
    <div id="rightside">

    	<!-- Status Bar Start -->
        <div class="status warning">
        	<p class="closestatus"><a href="" title="Close">x</a></p>
        	<p><img src="img/icons/icon_warning.png" alt="Warning" /><span>Attention!</span> Lorem ipsum dolor sit amet, consectetuer, sed diam nonummy nibh.</p>
        </div>
        <!-- Status Bar End -->

         <!-- Red Status Bar Start -->
        <div class="status success">
        	<p class="closestatus"><a href="" title="Close">x</a></p>
        	<p><img src="img/icons/icon_success.png" alt="Success" /><span>Success!</span> Lorem ipsum dolor sit amet, consectetuer adipiscing, sed diam nonummy nibh.</p>
        </div>
        <!-- Red Status Bar End -->

        <!-- Green Status Bar Start -->
        <div class="status error">
        	<p class="closestatus"><a href="" title="Close">x</a></p>
        	<p><img src="img/icons/icon_error.png" alt="Error" /><span>Error!</span> Lorem ipsum dolor sit amet, consectetuer adipiscing, sed diam nonummy nibh.</p>
        </div>
        <!-- Green Status Bar End -->

        <!-- Blue Status Bar Start -->
        <div class="status info">
        	<p class="closestatus"><a href="" title="Close">x</a></p>
        	<p><img src="img/icons/icon_info.png" alt="Information" /><span>Information:</span> Lorem ipsum dolor sit amet, consectetuer adipiscing, sed diam nonummy nibh.</p>
        </div>
        <!-- Blue Status Bar End -->

        <!-- Content Box Start -->
        <div class="contentcontainer">
            <div class="headings alt">
                <h2>Usage bar exmaples</h2>
            </div>
            <div class="contentbox">
            	<table>
                	<tr>
                    	<td width="150"><strong><span class="usagetxt redtxt">Red</span> usage bar:</strong></td>
                        <td width="500">
                        	<div class="usagebox">
                				<div class="highbar" style="width: 85%;"></div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                    	<td><strong><span class="usagetxt orangetxt">Orange</span> usage bar:</strong></td>
                        <td>
                        	<div class="usagebox">
                				<div class="midbar" style="width: 50%;"></div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                    	<td><strong><span class="usagetxt greentxt">Green</span> usage bar:</strong></td>
                        <td>
                        	<div class="usagebox">
                				<div class="lowbar" style="width: 25%;"></div>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <!-- Content Box End -->

         <!-- Graphs Box Start -->
        <div class="contentcontainer" id="graphs">
            <div class="headings alt">
                <h2 class="left">Dynamic graph types</h2>
                <ul class="smltabs">
                	<li><a href="#graphs-1">Area chart</a></li>
                    <li><a href="#graphs-2">Pie chart</a></li>
                    <li><a href="#graphs-3">Line graph</a></li>
                    <li><a href="#graphs-4">Bar graph</a></li>
                </ul>
            </div>
            <div class="contentbox" id="graphs-1">
					<table style="display: none;" class="area">
                        <caption> Registered Members</caption>
                        <thead>
                            <tr>
                                <td></td>
                                <th scope="col">Jan</th>
                                <th scope="col">Feb</th>
                                <th scope="col">March</th>
                                <th scope="col">April</th>
                                <th scope="col">May</th>
                                <th scope="col">June</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th scope="row">2010</th>
                                <td>190</td>
                                <td>160</td>
                                <td>40</td>
                                <td>120</td>
                                <td>30</td>
                                <td>70</td>
                            </tr>
                            <tr>
                                <th scope="row">2009</th>
                                <td>3</td>
                                <td>40</td>
                                <td>30</td>
                                <td>45</td>
                                <td>35</td>
                                <td>49</td>
                            </tr>
                        </tbody>
                    </table>
            </div>
            <div class="contentbox" id="graphs-2">
            	<table style="display: none;" class="pie">
                    <caption> Registered Members</caption>
                        <thead>
                            <tr>
                                <td></td>
                                <th scope="col">Jan</th>
                                <th scope="col">Feb</th>
                                <th scope="col">March</th>
                                <th scope="col">April</th>
                                <th scope="col">May</th>
                                <th scope="col">June</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th scope="row">2010</th>
                                <td>190</td>
                                <td>160</td>
                                <td>40</td>
                                <td>120</td>
                                <td>30</td>
                                <td>70</td>
                            </tr>
                            <tr>
                                <th scope="row">2009</th>
                                <td>3</td>
                                <td>40</td>
                                <td>30</td>
                                <td>45</td>
                                <td>35</td>
                                <td>49</td>
                            </tr>
                        </tbody>
                    </table>
            </div>
            <div class="contentbox" id="graphs-3">
					<table style="display: none;" class="line">
                        <caption> Registered Members</caption>
                        <thead>
                            <tr>
                                <td></td>
                                <th scope="col">Jan</th>
                                <th scope="col">Feb</th>
                                <th scope="col">March</th>
                                <th scope="col">April</th>
                                <th scope="col">May</th>
                                <th scope="col">June</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th scope="row">2010</th>
                                <td>190</td>
                                <td>160</td>
                                <td>40</td>
                                <td>120</td>
                                <td>30</td>
                                <td>70</td>
                            </tr>
                            <tr>
                                <th scope="row">2009</th>
                                <td>3</td>
                                <td>40</td>
                                <td>30</td>
                                <td>45</td>
                                <td>35</td>
                                <td>49</td>
                            </tr>
                        </tbody>
                    </table>
            </div>
            <div class="contentbox" id="graphs-4">
					<table style="display: none;" class="bar">
                       <caption> Registered Members</caption>
                        <thead>
                            <tr>
                                <td></td>
                                <th scope="col">Jan</th>
                                <th scope="col">Feb</th>
                                <th scope="col">March</th>
                                <th scope="col">April</th>
                                <th scope="col">May</th>
                                <th scope="col">June</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th scope="row">2010</th>
                                <td>190</td>
                                <td>160</td>
                                <td>40</td>
                                <td>120</td>
                                <td>30</td>
                                <td>70</td>
                            </tr>
                            <tr>
                                <th scope="row">2009</th>
                                <td>3</td>
                                <td>40</td>
                                <td>30</td>
                                <td>45</td>
                                <td>35</td>
                                <td>49</td>
                            </tr>
                        </tbody>
                    </table>
            </div>
        </div>
        <!-- Graphs Box End -->

        <!-- Alternative Content Box Start -->
         <div class="contentcontainer">
            <div class="headings altheading">
                <h2>Alternative coloured heading</h2>
            </div>
            <div class="contentbox">
            	<table width="100%">
                	<thead>
                    	<tr>
                        	<th>Heading</th>
                            <th>Another Heading</th>
                            <th>Actions</th>
                            <th><input name="" type="checkbox" value="" id="checkboxall" /></th>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr>
                        	<td>Content Here</td>
                            <td>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam.</td>
                            <td>
                            	<a href="#" title=""><img src="img/icons/icon_edit.png" alt="Edit" /></a>
                            	<a href="#" title=""><img src="img/icons/icon_approve.png" alt="Approve" /></a>
                                <a href="#" title=""><img src="img/icons/icon_unapprove.png" alt="Unapprove" /></a>
                                <a href="#" title=""><img src="img/icons/icon_delete.png" alt="Delete" /></a>
                            </td>
                            <td><input type="checkbox" value="" name="checkall" /></td>
                        </tr>
                        <tr class="alt">
                        	<td>Content Here</td>
                            <td>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam.</td>
                            <td>
                            	<a href="#" title=""><img src="img/icons/icon_edit.png" alt="Edit" /></a>
                            	<a href="#" title=""><img src="img/icons/icon_approve.png" alt="Approve" /></a>
                                <a href="#" title=""><img src="img/icons/icon_unapprove.png" alt="Unapprove" /></a>
                                <a href="#" title=""><img src="img/icons/icon_delete.png" alt="Delete" /></a>
                            </td>
                            <td><input type="checkbox" value="" name="checkall" /></td>
                        </tr>
                        <tr>
                        	<td>Content Here</td>
                            <td>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam.</td>
                            <td>
                            	<a href="#" title=""><img src="img/icons/icon_edit.png" alt="Edit" /></a>
                            	<a href="#" title=""><img src="img/icons/icon_approve.png" alt="Approve" /></a>
                                <a href="#" title=""><img src="img/icons/icon_unapprove.png" alt="Unapprove" /></a>
                                <a href="#" title=""><img src="img/icons/icon_delete.png" alt="Delete" /></a>
                            </td>
                            <td><input type="checkbox" value="" name="checkall" /></td>
                        </tr>
                         <tr class="alt">
                        	<td>Content Here</td>
                            <td>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam.</td>
                            <td>
                            	<a href="#" title=""><img src="img/icons/icon_edit.png" alt="Edit" /></a>
                            	<a href="#" title=""><img src="img/icons/icon_approve.png" alt="Approve" /></a>
                                <a href="#" title=""><img src="img/icons/icon_unapprove.png" alt="Unapprove" /></a>
                                <a href="#" title=""><img src="img/icons/icon_delete.png" alt="Delete" /></a>
                            </td>
                            <td><input type="checkbox" value="" name="checkall" /></td>
                        </tr>
                    </tbody>
                </table>
                <div class="extrabottom">
                	<ul>
                    	<li><img src="img/icons/icon_edit.png" alt="Edit" /> Edit</li>
                        <li><img src="img/icons/icon_approve.png" alt="Approve" /> Approve</li>
                        <li><img src="img/icons/icon_unapprove.png" alt="Unapprove" /> Unapprove</li>
                        <li><img src="img/icons/icon_delete.png" alt="Delete" /> Remove</li>
                    </ul>
                    <div class="bulkactions">
                    	<select>
                        	<option>Select bulk action...</option>
                        </select>
                        <input type="submit" value="Apply" class="btn" />
                    </div>
                </div>
                <ul class="pagination">
                	<li class="text">Previous</li>
                    <li class="page"><a href="#" title="">1</a></li>
                    <li><a href="#" title="">2</a></li>
                    <li><a href="#" title="">3</a></li>
                    <li><a href="#" title="">4</a></li>
                    <li class="text"><a href="#" title="">Next</a></li>
                </ul>
                <div style="clear: both;"></div>
            </div>

        </div>
        <!-- Alternative Content Box End -->

         <div class="contentcontainer med left">
            <div class="headings alt">
                <h2>Medium Width - Heading title here</h2>
            </div>
            <div class="contentbox">
            	<form action="#">
            		<p>
                        <label for="textfield"><strong>Text field:</strong></label>
                        <input type="text" id="textfield" class="inputbox" /> <br />
                        <span class="smltxt">(This is an example of a small descriptive text for input)</span>
                    </p>
                    <p>
                        <label for="errorbox"><span class="red"><strong>Missing field:</strong></span></label>
                        <input type="text" id="errorbox" class="inputbox errorbox" /> <img src="img/icons/icon_missing.png" alt="Error" /> <br />
                        <span class="smltxt red">(This is some warning text for the above field)</span>
                    </p>
                    <p>
                        <label for="correctbox"><span class="green"><strong>Correct field:</strong></span></label>
                        <input type="text" id="correctbox" class="inputbox correctbox" /> <img src="img/icons/icon_approve.png" alt="Approve" />
                    </p>
                    <p>
                        <label for="smallbox"><strong>Small Text field:</strong></label>
                        <input type="text" id="smallbox" class="inputbox smallbox" />
                    </p>
                    <select>
                    	<option>Dropdown Menu Example</option>
                    	<option>Dropdown Menu Example</option>
                    	<option>Dropdown Menu Example</option>
                    </select>  <br /> <br />

                    <input type="file" id="uploader" /> <img src="img/loading.gif" alt="Loading" /> Uploading...

                    <p> <br />
                    	<input type="checkbox" name="checkboxexample"/> Checkbox
					</p>
                    <p>
                        <input type="radio" name="radioexample" /> Radio select<br />
                    </p>

                </form>
				<textarea class="text-input textarea" id="wysiwyg" name="textfield" rows="10" cols="75"></textarea>
                <p><br /><br />Buttons styles</p>
                <input type="submit" value="Submit" class="btn" /> <input type="submit" value="Submit (Alternative)" class="btnalt" />
            </div>
        </div>

         <div class="contentcontainer sml right" id="tabs">
            <div class="headings">
                <h2 class="left">Tabs</h2>
                <ul class="smltabs">
                	<li><a href="#tabs-1">Styles</a></li>
                    <li><a href="#tabs-2">News</a></li>
                </ul>
            </div>
            <div class="contentbox" id="tabs-1">
            	<h2 style="margin-bottom: 15px;">Some styling examples below:</h2>
                <p><strong>List style one</strong></p>
                <ul class="list">
                	<li>Lorem ipsum dolor sit amet, consectetuer adipiscing elit</li>
                    <li>Consectetuer adipiscing elit</li>
                    <li>Sed diam nonummy nibh euismod tincidunt</li>
                </ul>
                <div class="spacer"></div>
                <p><strong>List style two</strong></p>
                <ul class="ticklist">
                	<li>Lorem ipsum dolor sit amet, consectetuer adipiscing elit</li>
                    <li class="cross">Consectetuer adipiscing elit</li>
                    <li>Sed diam nonummy nibh euismod tincidunt</li>
                </ul>

                <p>Lorem ipsum dolor sit amet, <span class="highlighted">highlighted</span>, sed diam nonummy nibh euismod tincidunt.</p>

                <p>Faded spacer example:</p>
                <div class="spacer"></div>

                <p><span class="dropcap">D</span>rop cap example to make your text stand out more! Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt.</p>
                 <div class="spacer"></div>
                <h1> Heading 1 example</h1>
                <div class="spacer"></div>
                <h2>Heading 2 example</h2>
                <div class="spacer"></div>
                <h3>Heading 3 example</h3>
                <div class="spacer"></div>
                <h4>Heading 4 example</h4>
                <div class="spacer"></div>
                <h5>Heading 5 example</h5>
            </div>
             <div class="contentbox nopad" id="tabs-2">

             	<div class="newsitem">
                    <img src="img/news_temp.png" class="hoverimg" alt="News" />
                    <h3>Story 1 Example Title</h3>
                    <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed...<br /><a href="#" title="">Read full story</a></p>
                </div>
                <div class="newsitem">
                    <img src="img/news_temp.png" class="hoverimg" alt="News" />
                    <h3>Story 2 Example Title</h3>
                    <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed...<br /><a href="#" title="">Read full story</a></p>
                </div>
                <div class="newsitem">
                    <img src="img/news_temp.png" class="hoverimg" alt="News" />
                    <h3>Story 3 Example Title</h3>
                    <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed...<br /><a href="#" title="">Read full story</a></p>
                </div>
                <div class="newsitem">
                    <img src="img/news_temp.png" class="hoverimg" alt="News" />
                    <h3>Story 4 Example Title</h3>
                    <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed...<br /><a href="#" title="">Read full story</a></p>
                </div>
                <p class="bottominfo"><a href="#" title="">See all news stories</a></p>
            </div>
        </div>

        <div style="clear:both;"></div>

        <!-- Content Box Start -->
        <div class="contentcontainer">
            <div class="headings">
                <h2>Notice Box Styles</h2>
            </div>
            <div class="contentbox">
                <div class="noticebox">
                    <div class="innernotice">
                        <h4>Yellow Notice Box</h4>
                        <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.</p>
                        <p>Sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.</p>
                        <p><a href="#" title="">Lincidunt ut laoreet dolore</a></p>
                    </div>
                </div>
                <div class="noticeboxalt">
                    <div class="innernotice">
                        <h4>Grey Notice Box</h4>
                        <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.</p>
                        <p>Sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.</p>
                        <p><a href="#" title="">Lincidunt ut laoreet dolore</a></p>
                    </div>
                </div>
                <div style="clear: both;"></div>
            </div>
        </div>
        <!-- Content Box End -->
        <div id="footer">
        	&copy; Copyright 2010 Your Company Name
        </div>

    </div>
    <!-- Right Side/Main Content End -->

        <!-- Left Dark Bar Start -->
    <div id="leftside">
    	<div class="user">
        	<img src="img/avatar.png" width="44" height="44" class="hoverimg" alt="Avatar" />
            <p>Logged in as:</p>
            <p class="username">Administrator</p>
            <p class="userbtn"><a href="#" title="">Profile</a></p>
            <p class="userbtn"><a href="#" title="">Log out</a></p>
        </div>
        <div class="notifications">
        	<p class="notifycount"><a href="" title="" class="notifypop">10</a></p>
            <p><a href="" title="" class="notifypop">New Notifications</a></p>
            <p class="smltxt">(Click to open notifications)</p>
        </div>

        <ul id="nav">
        	<li>
                <ul class="navigation">
                    <li class="heading selected">流量查看</li>
                  <li><a href="#" title="">流媒体流量查看</a></li>
                    <li><a href="#" title="">网页流量查看</a></li>
                    <li><a href="#" title="">上传加速流量查看</a></li>
                </ul>
            </li>
            <li>
                <a class="collapsed heading">带宽统计</a>
                 <ul class="navigation">
                    <li><a href="#" title="">流媒体带宽统计</a></li>
                    <li><a href="#" title="">网页带宽统计</a></li>
                    <li><a href="#" title="">上传带宽统计</a></li>
                </ul>
            </li>
            <li><a class="expanded heading">数据分析</a>
                <ul class="navigation">
                    <li><a href="#" title="" class="likelogin">上传加速分析</a></li>
                    <li><a href="#" title="">流媒体分析</a></li>
                    <li><a href="#" title="">网页分析</a></li>
                </ul>
            </li>
        </ul>
    </div>
    <!-- Left Dark Bar End -->

    <!-- Notifications Box/Pop-Up Start -->
    <div id="notificationsbox">
        <h4>Notifications</h4>
        <ul>
            <li>
            	<a href="#" title=""><img src="img/icons/icon_square_close.png" alt="Close" class="closenot" /></a>
            	<h5><a href="#" title="">New member registration</a></h5>
                <p>Admin eve joined on 18.12.2010</p>
            </li>
            <li>
            	<a href="#" title=""><img src="img/icons/icon_square_close.png" alt="Close" class="closenot" /></a>
            	<h5><a href="#" title="">New member registration</a></h5>
                <p>Jackson Michael joined on 16.12.2010</p>
            </li>
            <li>
            	<a href="#" title=""><img src="img/icons/icon_square_close.png" alt="Close" class="closenot" /></a>
                <h5><a href="#" title="">New blog post created</a></h5>
                <p>New post created on 15.12.2010</p>
            </li>
            <li>
            	<a href="#" title=""><img src="img/icons/icon_square_close.png" alt="Close" class="closenot" /></a>
            	<h5><a href="#" title="">New group created</a></h5>
                <p>“Web Design” group created on 12.12.2010</p>
            </li>
            <li>
            	<a href="#" title=""><img src="img/icons/icon_square_close.png" alt="Close" class="closenot" /></a>
            	<h5><a href="#" title="">1 new private message</a></h5>
                <p>New message from Joe sent on 21.11.2010</p>
            </li>
            <li>
            	<a href="#" title=""><img src="img/icons/icon_square_close.png" alt="Close" class="closenot" /></a>
            	<h5><a href="#" title="">New member registration</a></h5>
                <p>Graham joined on 20.11.2010</p>
            </li>
        </ul>
        <p class="loadmore"><a href="#" title="">Load more notifications</a></p>
    </div>
    <!-- Notifications Box/Pop-Up End -->

    <script type="text/javascript" src="http://dwpe.googlecode.com/svn/trunk/_shared/EnhanceJS/enhance.js"></script>
    <script type='text/javascript' src='http://dwpe.googlecode.com/svn/trunk/charting/js/excanvas.js'></script>
	<script type='text/javascript' src='https://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js'></script>
    <script type='text/javascript' src='https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.6/jquery-ui.min.js'></script>
	<script type='text/javascript' src='scripts/jquery.wysiwyg.js'></script>
    <script type='text/javascript' src='scripts/visualize.jQuery.js'></script>
    <script type="text/javascript" src='scripts/functions.js'></script>

    <!--[if IE 6]>
    <script type='text/javascript' src='scripts/png_fix.js'></script>
    <script type='text/javascript'>
      DD_belatedPNG.fix('img, .notifycount, .selected');
    </script>
    <![endif]-->
</body>
</html>
