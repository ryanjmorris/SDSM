using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using System.Data.SqlClient;
using System.Data;
using System.Web;
using System.Web.Script.Serialization;
using System.Web.Script.Services;
using System.Web.Services;

namespace SDSMWebService
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Service1.svc or Service1.svc.cs at the Solution Explorer and start debugging.
    public class Service1 : IService1
    {
        public List<signedUpUser> GetData(string userName, string passWord)
        {
            List<signedUpUser> users = new List<signedUpUser>();
            try
            {
                using (SqlConnection conn = new SqlConnection("Data Source = SDMSA.db.11959136.hostedresource.com;Initial Catalog=SDMSA;User ID=SDMSA;Password=REDACTED"))
                {
                    conn.Open();

                    string s = "select id, fname, lname, username, state, email, password from SDMSA.dbo.SDSMUsers";
                    if (userName.Equals("") && passWord.Equals(""))
                    {

                    }
                    else
                    {
                        s += " where username = '" + userName + "' AND password = '"+passWord+"'";
                    }
                    using (SqlCommand command = new SqlCommand(s, conn))
                    {
                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                signedUpUser suu = new signedUpUser();
                                suu.IDENTIFICATION = reader.GetInt32(0);
                                suu.FIRSTNAME = reader.GetString(1);
                                suu.LASTNAME = reader.GetString(2);
                                suu.USERNA = reader.GetString(3);
                                suu.THESTATE = reader.GetString(4);
                                suu.THEEMAIL = reader.GetString(5);
                                suu.THEPASSWORD = reader.GetString(6);

                                users.Add(suu);
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                signedUpUser suu = new signedUpUser();
                suu.IDENTIFICATION = 0;
                suu.FIRSTNAME = e.Message;
                suu.LASTNAME = e.Message;
                suu.USERNA = e.Message;
                suu.THESTATE = e.Message;
                suu.THEEMAIL = e.Message;
                suu.THEPASSWORD = e.Message;

                users.Add(suu);
            }
            return users;
        }

        public List<signedUpUser> GetUser(string singUser)
        {
            List<signedUpUser> singleUser = new List<signedUpUser>();
            try
            {
                using (SqlConnection conn = new SqlConnection("Data Source = SDMSA.db.11959136.hostedresource.com;Initial Catalog=SDMSA;User ID=SDMSA;Password=REDACTED"))
                {
                    conn.Open();

                    string s = "select username from SDMSA.dbo.SDSMUsers";
                    if (singUser.Equals(""))
                    {

                    }
                    else
                    {
                        s += " where username = '" + singUser + "'";
                    }
                    using (SqlCommand command = new SqlCommand(s, conn))
                    {
                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                signedUpUser suu = new signedUpUser();
                                suu.USERNA = reader.GetString(0);

                                singleUser.Add(suu);
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                signedUpUser suu = new signedUpUser();
                suu.USERNA = e.Message;

                singleUser.Add(suu);
            }
            return singleUser;
        }

        public List<SignedUp> InsertUserDetails(string firstName, string lastName, string theUser, string theState, string theEmail, string thePassword)
        {
            string Info;
            List<SignedUp> useri = new List<SignedUp>();
            using (SqlConnection con = new SqlConnection("Data Source = SDMSA.db.11959136.hostedresource.com;Initial Catalog=SDMSA;User ID=SDMSA;Password=REDACTED"))
            {
                con.Open();

                SignedUp sU = new SignedUp();
                sU.FIRSTNAME = firstName;
                sU.LASTNAME = lastName;
                sU.USER = theUser;
                sU.THESTATE = theState;
                sU.THEEMAIL = theEmail;
                sU.THEPASSWORD = thePassword;

                SqlCommand cmd = new SqlCommand("INSERT INTO SDSMUsers(FName, LName, Username, State, Email, Password) VALUES(@FName, @LName, @Username, @State, @Email, @Password)", con);
                cmd.Parameters.AddWithValue("@FName", sU.FIRSTNAME);
                cmd.Parameters.AddWithValue("@LName", sU.LASTNAME);
                cmd.Parameters.AddWithValue("@Username", sU.USER);
                cmd.Parameters.AddWithValue("@State", sU.THESTATE);
                cmd.Parameters.AddWithValue("@Email", sU.THEEMAIL);
                cmd.Parameters.AddWithValue("@Password", sU.THEPASSWORD);

                int result = cmd.ExecuteNonQuery();
                if (result == 1)
                {
                    Info = " Details inserted succesfully.";
                }
                else
                {
                    Info = " Details not inserted successfully";
                }
                con.Close();
                return useri;
            }
        }

        public List<setPaymentInfo> InsertUserPayment(string theuserName, string thecreditCard, string thecardType, string theexpiriation, string thesecurityCode)
        {
            List<setPaymentInfo> UserPayment = new List<setPaymentInfo>();
            using (SqlConnection con = new SqlConnection("Data Source = SDMSA.db.11959136.hostedresource.com;Initial Catalog=SDMSA;User ID=SDMSA;Password=REDACTED"))
            {
                con.Open();

                setPaymentInfo setUserPayment = new setPaymentInfo();
                setUserPayment.USERNAME = theuserName;
                setUserPayment.CREDITCARD = thecreditCard;
                setUserPayment.CARDTYPE = thecardType;
                setUserPayment.EXPIRATION = theexpiriation;
                setUserPayment.SECURITYCARD = thesecurityCode;

                SqlCommand cmd = new SqlCommand("INSERT INTO SDSMPurchase(Username, CreditCard, CreditType, Expiration, SecurityCode) VALUES(@Username, @CreditCard, @CreditType, @Expiration, @SecurityCode)", con);
                cmd.Parameters.AddWithValue("@Username", setUserPayment.USERNAME);
                cmd.Parameters.AddWithValue("@CreditCard", setUserPayment.CREDITCARD);
                cmd.Parameters.AddWithValue("@CreditType", setUserPayment.CARDTYPE);
                cmd.Parameters.AddWithValue("@Expiration", setUserPayment.EXPIRATION);
                cmd.Parameters.AddWithValue("@SecurityCode", setUserPayment.SECURITYCARD);
                cmd.ExecuteNonQuery();

                SqlCommand cmd2 = new SqlCommand("UPDATE SDSMCholors SET PaidAccount='1' WHERE Username=@Username", con);
                cmd2.Parameters.AddWithValue("@Username", setUserPayment.USERNAME);
                cmd2.ExecuteNonQuery();

                con.Close();
                return UserPayment;
            }
        }

        public List<insertCholorsHighScore> InsertCholorsHighScore(string theuserName, int thehighScore, string ispaidAccount, string theCost)
        {
            List<insertCholorsHighScore> CholorHiScore = new List<insertCholorsHighScore>();
            using (SqlConnection con = new SqlConnection("Data Source = SDMSA.db.11959136.hostedresource.com;Initial Catalog=SDMSA;User ID=SDMSA;Password=REDACTED"))
            {
                con.Open();

                insertCholorsHighScore setHighScore = new insertCholorsHighScore();
                setHighScore.USERNAME = theuserName;
                setHighScore.HIGHSCORE = thehighScore;
                setHighScore.PAIDACCOUNT = ispaidAccount;
                setHighScore.COST = theCost;

                string checkPaid = "SELECT TOP 1 Username, PaidAccount FROM SDMSA.dbo.SDSMCholors WHERE PaidAccount = '1' AND Username = '" + theuserName + "'";
                using (SqlCommand command = new SqlCommand(checkPaid, con))
                {
                    //SqlCommand cmd3 = new SqlCommand("SELECT Username, PaidAccount FROM SDMSA.dbo.SDSMCholors WHERE PaidAccount = '1' AND Username = '"++"'", con);
                    //cmd3.Parameters.AddWithValue("@Username", setHighScore.USERNAME);

                    //cmd3.ExecuteNonQuery();
                        if (command.ExecuteNonQuery().Equals(1))
                        {
                            SqlCommand cmd = new SqlCommand("INSERT INTO SDSMCholors(Username, HighScore, PaidAccount, Cost) VALUES(@Username, @HighScore, @PaidAccount, @Cost)", con);
                            cmd.Parameters.AddWithValue("@Username", setHighScore.USERNAME);
                            cmd.Parameters.AddWithValue("@HighScore", setHighScore.HIGHSCORE);
                            cmd.Parameters.AddWithValue("@PaidAccount", setHighScore.PAIDACCOUNT);
                            cmd.Parameters.AddWithValue("@Cost", setHighScore.COST);
                            cmd.ExecuteNonQuery();

                            SqlCommand cmd2 = new SqlCommand("UPDATE SDSMCholors SET PaidAccount='1' WHERE Username=@Username", con);
                            cmd2.Parameters.AddWithValue("@Username", setHighScore.USERNAME);
                            cmd2.ExecuteNonQuery();

                            con.Close();
                        }
                        else
                        {
                            SqlCommand cmd2 = new SqlCommand("INSERT INTO SDSMCholors(Username, HighScore, PaidAccount, Cost) VALUES(@Username, @HighScore, @PaidAccount, @Cost)", con);
                             cmd2.Parameters.AddWithValue("@Username", setHighScore.USERNAME);
                            cmd2.Parameters.AddWithValue("@HighScore", setHighScore.HIGHSCORE);
                            cmd2.Parameters.AddWithValue("@PaidAccount", setHighScore.PAIDACCOUNT);
                            cmd2.Parameters.AddWithValue("@Cost", setHighScore.COST);
                            cmd2.ExecuteNonQuery();

                            con.Close();
                        }
                }

                return CholorHiScore;
            }
        }

        public List<insertCholorsHighScore> CheckIfPaidAccount(string theuserName)
        {
            List<insertCholorsHighScore> checkIfPaid = new List<insertCholorsHighScore>();
            try
            {
                using (SqlConnection conn = new SqlConnection("Data Source = SDMSA.db.11959136.hostedresource.com;Initial Catalog=SDMSA;User ID=SDMSA;Password=REDACTED"))
                {
                    conn.Open();

                    string s = "select top 1 PaidAccount from SDMSA.dbo.SDSMCholors";
                    if (theuserName.Equals(""))
                    {

                    }
                    else
                    {
                        s += " where Username = '" + theuserName + "'";
                    }
                    using (SqlCommand command = new SqlCommand(s, conn))
                    {
                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                insertCholorsHighScore suu = new insertCholorsHighScore();
                                suu.PAIDACCOUNT = reader.GetString(0);

                                checkIfPaid.Add(suu);
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                insertCholorsHighScore suu = new insertCholorsHighScore();
                suu.PAIDACCOUNT = e.Message;

                checkIfPaid.Add(suu);
            }
            return checkIfPaid;
        }

        public List<insertCholorsHighScore> CholorsPersonalHighScore(string theuserName)
        {
            List<insertCholorsHighScore> personalHighScore = new List<insertCholorsHighScore>();
            try
            {
                using (SqlConnection conn = new SqlConnection("Data Source = SDMSA.db.11959136.hostedresource.com;Initial Catalog=SDMSA;User ID=SDMSA;Password=REDACTED"))
                {
                    conn.Open();

                    string s = "SELECT TOP 1 HighScore FROM SDMSA.dbo.SDSMCholors";
                    if (theuserName.Equals(""))
                    {

                    }
                    else
                    {
                        s += " where Username = '" + theuserName + "' ORDER BY HighScore DESC";
                    }
                    using (SqlCommand command = new SqlCommand(s, conn))
                    {
                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                insertCholorsHighScore suu = new insertCholorsHighScore();
                                suu.HIGHSCORE = reader.GetInt32(0);

                                personalHighScore.Add(suu);
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                insertCholorsHighScore suu = new insertCholorsHighScore();
                suu.HIGHSCORE = 0;

                personalHighScore.Add(suu);
            }
            return personalHighScore;
        }

        public List<insertCholorsHighScore> CholorsWorldHighScore(string theuserName)
        {
            List<insertCholorsHighScore> worldHighScore = new List<insertCholorsHighScore>();
            try
            {
                using (SqlConnection conn = new SqlConnection("Data Source = SDMSA.db.11959136.hostedresource.com;Initial Catalog=SDMSA;User ID=SDMSA;Password=REDACTED"))
                {
                    conn.Open();

                    string s = "SELECT TOP 1  SDSMUsers.Username, SDSMCholors.HighScore, SDSMUsers.State FROM SDMSA.dbo.SDSMCholors INNER JOIN SDSMUsers ON SDSMCholors.Username = SDSMUsers.Username ORDER BY HighScore DESC";
                    using (SqlCommand command = new SqlCommand(s, conn))
                    {
                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                signedUpUser suuu = new signedUpUser();
                                suuu.USERNA = reader.GetString(0);
                                insertCholorsHighScore suu = new insertCholorsHighScore();
                                suu.HIGHSCORE = reader.GetInt32(1);
                                suuu.THESTATE = reader.GetString(2);

                                worldHighScore.Add(suu);
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                signedUpUser suuu = new signedUpUser();
                suuu.USERNA = e.Message;
                insertCholorsHighScore suu = new insertCholorsHighScore();
                suu.HIGHSCORE = 0;
                suuu.THESTATE = e.Message;

                worldHighScore.Add(suu);
            }
            return worldHighScore;
        }

        public List<signedUpUser> CholorsWorldHiScore(string theuserName)
        {
            List<signedUpUser> worldHighScore2 = new List<signedUpUser>();
            try
            {
                using (SqlConnection conn = new SqlConnection("Data Source = SDMSA.db.11959136.hostedresource.com;Initial Catalog=SDMSA;User ID=SDMSA;Password=REDACTED"))
                {
                    conn.Open();

                    string s = "SELECT TOP 1  SDSMUsers.Username, SDSMCholors.HighScore, SDSMUsers.State FROM SDMSA.dbo.SDSMCholors INNER JOIN SDSMUsers ON SDSMCholors.Username = SDSMUsers.Username ORDER BY HighScore DESC";
                    using (SqlCommand command = new SqlCommand(s, conn))
                    {
                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                signedUpUser suuu = new signedUpUser();
                                suuu.USERNA = reader.GetString(0);
                                insertCholorsHighScore suu = new insertCholorsHighScore();
                                suu.HIGHSCORE = reader.GetInt32(1);
                                suuu.THESTATE = reader.GetString(2);

                                worldHighScore2.Add(suuu);
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                signedUpUser suuu = new signedUpUser();
                suuu.USERNA = e.Message;
                insertCholorsHighScore suu = new insertCholorsHighScore();
                suu.HIGHSCORE = 0;
                suuu.THESTATE = e.Message;

                worldHighScore2.Add(suuu);
            }
            return worldHighScore2;
        }

        public List<insertFlashlightClick> InsertFlashlightClick(string theuserName, int theClick, int thepaidAccount, double theCost)
        {
            List<insertFlashlightClick> amountClicked = new List<insertFlashlightClick>();
            try
            {
                using (SqlConnection conn = new SqlConnection("Data Source = SDMSA.db.11959136.hostedresource.com;Initial Catalog=SDMSA;User ID=SDMSA;Password=REDACTED"))
                {
                    conn.Open();
                    
                    insertFlashlightClick ifc = new insertFlashlightClick();
                    ifc.USERNAME = theuserName;
                    ifc.CLICK = theClick;
                    ifc.ISPAIDACCOUNT = thepaidAccount;
                    ifc.COST = theCost;

                    string s = "SELECT Username FROM SDMSA.dbo.SDSMFlashlight";
                    if (theuserName.Equals(""))
                    {

                    }
                    else
                    {
                        s += " where Username = '" + theuserName + "'";
                    }

                    using (SqlCommand command = new SqlCommand(s, conn))
                    {
                        SqlCommand cmd = new SqlCommand("INSERT INTO SDSMFlashlight(Username, Clicked, PaidAccount, Cost) VALUES(@Username, @Clicked, @PaidAccount, @Cost)", conn);
                        cmd.Parameters.AddWithValue("@Username", ifc.USERNAME);
                        cmd.Parameters.AddWithValue("@Clicked", ifc.CLICK);
                        cmd.Parameters.AddWithValue("@PaidAccount", ifc.ISPAIDACCOUNT);
                        cmd.Parameters.AddWithValue("@Cost", ifc.COST);
                        cmd.ExecuteNonQuery();

                        conn.Close();
                    }
                }
            }
            catch (Exception e)
            {

            }

            return amountClicked;
        }

        public List<insertFlashlightClick> AmountClicked(string theuserName)
        {
            List<insertFlashlightClick> amountClicked = new List<insertFlashlightClick>();
            try
            {
                using (SqlConnection conn = new SqlConnection("Data Source = SDMSA.db.11959136.hostedresource.com;Initial Catalog=SDMSA;User ID=SDMSA;Password=REDACTED"))
                {
                    conn.Open();

                    string s = "SELECT Username, PaidAccount, SUM(Clicked) AS [Clicked] FROM SDSMFlashlight WHERE Username = '" + theuserName + "' GROUP BY Username, PaidAccount, Cost";
                    using (SqlCommand command = new SqlCommand(s, conn))
                    {
                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                insertFlashlightClick ifc = new insertFlashlightClick();
                                ifc.USERNAME = reader.GetString(0);
                                ifc.ISPAIDACCOUNT = reader.GetInt32(1);
                                ifc.CLICK = reader.GetInt32(2);
                                //ifc.COST = reader.GetInt32(3);

                                amountClicked.Add(ifc);
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                insertFlashlightClick ifc = new insertFlashlightClick();
                ifc.USERNAME = e.Message;
                ifc.CLICK = 0;
                ifc.ISPAIDACCOUNT = 0;
                ifc.COST = 0;

                amountClicked.Add(ifc);
            }
            return amountClicked;
        }

        public List<setPaymentInfoFlashlight> InsertFlashlightPayment(string theuserName, string thecreditCard, string thecardType, string theexpiration, string thesecurityCode)
        {
            List<setPaymentInfoFlashlight> UserPay = new List<setPaymentInfoFlashlight>();
            using (SqlConnection con = new SqlConnection("Data Source = SDMSA.db.11959136.hostedresource.com;Initial Catalog=SDMSA;User ID=SDMSA;Password=REDACTED"))
            {
                con.Open();

                setPaymentInfoFlashlight spi = new setPaymentInfoFlashlight();
                spi.USERNAME = theuserName;
                spi.CREDITCARD = thecreditCard;
                spi.CARDTYPE = thecardType;
                spi.EXPIRATION = theexpiration;
                spi.SECURITYCARD = thesecurityCode;

                SqlCommand cmd = new SqlCommand("INSERT INTO SDSMPurchase(Username, CreditCard, CreditType, Expiration, SecurityCode) VALUES(@Username, @CreditCard, @CreditType, @Expiration, @SecurityCode)", con);
                cmd.Parameters.AddWithValue("@Username", spi.USERNAME);
                cmd.Parameters.AddWithValue("@CreditCard", spi.CREDITCARD);
                cmd.Parameters.AddWithValue("@CreditType", spi.CARDTYPE);
                cmd.Parameters.AddWithValue("@Expiration", spi.EXPIRATION);
                cmd.Parameters.AddWithValue("@SecurityCode", spi.SECURITYCARD);
                cmd.ExecuteNonQuery();

                SqlCommand cmd2 = new SqlCommand("UPDATE SDSMFlashlight SET PaidAccount='1' WHERE Username=@Username", con);
                cmd2.Parameters.AddWithValue("@Username", spi.USERNAME);
                cmd2.ExecuteNonQuery();

                con.Close();
                return UserPay;
            }
        }

        public List<insertFlashlightClick> CheckIfPaidFlashlightAccount(string theuserName)
        {
            List<insertFlashlightClick> checkIfFlashlightPaid = new List<insertFlashlightClick>();
            try
            {
                using (SqlConnection conn = new SqlConnection("Data Source = SDMSA.db.11959136.hostedresource.com;Initial Catalog=SDMSA;User ID=SDMSA;Password=REDACTED"))
                {
                    conn.Open();

                    string s = "select top 1 PaidAccount from SDMSA.dbo.SDSMFlashlight";
                    if (theuserName.Equals(""))
                    {

                    }
                    else
                    {
                        s += " where Username = '" + theuserName + "'";
                    }
                    using (SqlCommand command = new SqlCommand(s, conn))
                    {
                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                insertFlashlightClick ifc = new insertFlashlightClick();
                                ifc.ISPAIDACCOUNT = reader.GetInt32(0);

                                checkIfFlashlightPaid.Add(ifc);
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                insertFlashlightClick ifc = new insertFlashlightClick();
                ifc.ISPAIDACCOUNT = 0;

                checkIfFlashlightPaid.Add(ifc);
            }
            return checkIfFlashlightPaid;
        }

    }

}
