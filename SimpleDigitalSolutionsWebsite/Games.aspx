<%@ Page Language="C#" AutoEventWireup="true" CodeFile="Games.aspx.cs" Inherits="Games" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <link rel="stylesheet" type="text/css" href="SDSGames.css" />
    <title>Games</title>
</head>
<body>
    <h1>Games</h1>
    <div id="Buttons">
        <button id="Home" onclick="location.href='Home.aspx'">Home</button>
        <button id="HighScores" onclick="location.href='HighScores.aspx'">High Scores</button>
        <button id="Users" onclick="location.href='Users.aspx'">Users</button>&nbsp;
    </div>
    <div style="position: fixed; z-index: -99; width: 100%; height: 100%">
       <iframe height="100%" width="100%" 
            src="https://www.youtube.com/v/SxoDd_WeXRU?autoplay=1&controls=0&showinfo=0&autohide=1&version=3&loop=1&playlist=SxoDd_WeXRU">
       </iframe>
    </div>
</body>
</html>
