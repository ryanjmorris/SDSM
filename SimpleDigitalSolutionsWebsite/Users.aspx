<%@ Page Language="C#" AutoEventWireup="true" CodeFile="Users.aspx.cs" Inherits="Users" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <link rel="stylesheet" type="text/css" href="SDSUsers.css" />
    <title></title>
</head>
<body>
    
    <h1>Users</h1>
    <div id="Menu">
        <button id="Home" onclick="location.href='Home.aspx'">Home</button>
        <button id="Games" onclick="location.href='Games.aspx'">Games</button>
        <button id="HighScores" onclick="location.href='HighScores.aspx'">High Scores</button>&nbsp;
    </div>
    <div id="Users">
        <asp:Label ID="CUsers" runat="server" Text="Current Users"></asp:Label>
        <asp:Label ID="PUsers" runat="server" Text="Paid Users"></asp:Label>
    </div>
    <form id="form1" runat="server">
        <div id="GroupGrid">
        <div id="Grid">
        <asp:GridView ID="GridView1" runat="server" AllowPaging="True" AutoGenerateColumns="False" DataSourceID="UserDataSource1" Width="248px">
            <Columns>
                <asp:BoundField DataField="FName" HeaderText="FName" SortExpression="FName" />
                <asp:BoundField DataField="LName" HeaderText="LName" SortExpression="LName" />
            </Columns>
        </asp:GridView>
        </div>
        <div id="Grid2">
            <asp:GridView ID="GridView2" runat="server" AllowPaging="True" AutoGenerateColumns="False" DataSourceID="UserDataSource1" Width="248px">
                <Columns>
                    <asp:BoundField DataField="FName" HeaderText="FName" SortExpression="FName" />
                    <asp:BoundField DataField="LName" HeaderText="LName" SortExpression="LName" />
                </Columns>
            </asp:GridView>
        </div>
        </div>
        <asp:SqlDataSource ID="UserDataSource1" runat="server" ConnectionString="<%$ ConnectionStrings:SDMSAConnectionString %>" SelectCommand="SELECT [FName], [LName] FROM [SDSMUsers] ORDER BY [LName]"></asp:SqlDataSource>   
     </form>
</body>
</html>
