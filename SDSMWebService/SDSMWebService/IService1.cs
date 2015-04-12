using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace SDSMWebService
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService1" in both code and config file together.
    [ServiceContract]
    public interface IService1
    {
        //This is for checking the Login piece, correct username/password
        [OperationContract]
        [WebInvoke(Method= "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "/json?username={userName}&password={passWord}")]
        List<signedUpUser> GetData(string userName, string passWord);

        //This is for checking the Username.
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "/jsonuser?username={singUser}")]
        List<signedUpUser> GetUser(string singUser);

        //This checks to see if the user has ever paid for a specific app
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "/jsonverified?username={theuserName}")]
        List<insertCholorsHighScore> CheckIfPaidAccount(string theuserName);

        //This grabs the high score for the individual user.
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "/jsoncholorpersonalhiscore?username={theuserName}")]
        List<insertCholorsHighScore> CholorsPersonalHighScore(string theuserName);

        //This grabs the high score for the entire world.
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "/jsoncholorworldhiscore?username={theuserName}")]
        List<insertCholorsHighScore> CholorsWorldHighScore(string theuserName);
        //List<signedUpUser> CholorsWorldHiScore(string theuserName);

        //Grabs how many times they have clicked on the Power button.
        [WebInvoke(Method = "GET",
           ResponseFormat = WebMessageFormat.Json,
           BodyStyle = WebMessageBodyStyle.Wrapped,
           UriTemplate = "/jsongetamountclicked?username={theuserName}")]
        List<insertFlashlightClick> AmountClicked(string theuserName);

        //This checks to see if the user has ever paid for the Flashlight application
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "/jsonflashlightverified?username={theuserName}")]
        List<insertFlashlightClick> CheckIfPaidFlashlightAccount(string theuserName);

        //Sign up portion, sends their personal details to SQL Server.
        [WebInvoke(Method = "POST",
            BodyStyle = WebMessageBodyStyle.WrappedRequest,
            ResponseFormat = WebMessageFormat.Json,
            RequestFormat = WebMessageFormat.Json,
            UriTemplate = "/jsonset?firstname={firstName}&lastname={lastName}&username={theUser}&state={theState}&email={theEmail}&password={thePassword}")]
        List<SignedUp> InsertUserDetails(string firstName, string lastName, string theUser, string theState, string theEmail, string thePassword);

        //Inserts the Credit Card information over to SQL Server.
        [WebInvoke(Method = "POST",
            BodyStyle = WebMessageBodyStyle.WrappedRequest,
            ResponseFormat = WebMessageFormat.Json,
            RequestFormat = WebMessageFormat.Json,
            UriTemplate = "/jsonpurchased?username={theuserName}&creditcard={thecreditCard}&cardtype={thecardType}&expiration={theexpiration}&securitycode={thesecurityCode}")]
        List<setPaymentInfo> InsertUserPayment(string theuserName, string thecreditCard, string thecardType, string theexpiration, string thesecurityCode);

        //Inserts the Credit Card information over to SQL Server and sets SDSMFlashlight to a PaidAccount.
        [WebInvoke(Method = "POST",
            BodyStyle = WebMessageBodyStyle.WrappedRequest,
            ResponseFormat = WebMessageFormat.Json,
            RequestFormat = WebMessageFormat.Json,
            UriTemplate = "/jsonpurchasedFlashlight?username={theuserName}&creditcard={thecreditCard}&cardtype={thecardType}&expiration={theexpiration}&securitycode={thesecurityCode}")]
        List<setPaymentInfoFlashlight> InsertFlashlightPayment(string theuserName, string thecreditCard, string thecardType, string theexpiration, string thesecurityCode);

        //Sends a personal users new high score over to the SQL Server.
        [WebInvoke(Method = "POST",
            BodyStyle = WebMessageBodyStyle.WrappedRequest,
            ResponseFormat = WebMessageFormat.Json,
            RequestFormat = WebMessageFormat.Json,
            UriTemplate = "/jsoncholorsinhiscore?username={theuserName}&highscore={thehighScore}&ispaidaccount={thepaidAccount}&cost={theCost}")]
        List<insertCholorsHighScore> InsertCholorsHighScore(string theuserName, int thehighScore, string thepaidAccount, string theCost);

        //Posts how many times the user has turned on/off the Flashlight application.
        [WebInvoke(Method = "POST",
            BodyStyle = WebMessageBodyStyle.WrappedRequest,
            ResponseFormat = WebMessageFormat.Json,
            RequestFormat = WebMessageFormat.Json,
            UriTemplate = "/jsonclickedpower?username={theuserName}&clicked={theClick}&paidaccount={thepaidAccount}&thecost={theCost}")]
        List<insertFlashlightClick> InsertFlashlightClick(string theuserName, int theClick, int thepaidAccount, double theCost);
    }

    [ServiceBehavior]
    // Use a data contract as illustrated in the sample below to add composite types to service operations.
    [DataContract]
    public class signedUpUser
    {
        int _identification;
        string lastname, firstname, userna, thestate, theemail, thepassword;

        [DataMember]
        public int IDENTIFICATION
        {
            get { return _identification; }
            set { _identification = value; }
        }

        [DataMember]
        public string LASTNAME
        {
            get { return lastname; }
            set { lastname = value; }
        }

        [DataMember]
        public string FIRSTNAME
        {
            get { return firstname; }
            set { firstname = value; }
        }

        [DataMember]
        public string USERNA
        {
            get { return userna; }
            set { userna = value; }
        }

        [DataMember]
        public string THESTATE
        {
            get { return thestate; }
            set { thestate = value; }
        }

        [DataMember]
        public string THEEMAIL
        {
            get { return theemail; }
            set { theemail = value; }
        }

        [DataMember]
        public string THEPASSWORD
        {
            get { return thepassword; }
            set { thepassword = value; }
        }
    }

    [DataContract]
    public class SignedUp
    {
        string firstname;
        string lastname;
        string user;
        string thestate;
        string theemail;
        string thepassword;

        [DataMember]
        public string FIRSTNAME
        {
            get { return firstname; }
            set { firstname = value; }
        }

        [DataMember]
        public string LASTNAME
        {
            get { return lastname; }
            set { lastname = value; }
        }

        [DataMember]
        public string USER
        {
            get { return user; }
            set { user = value; }
        }

        [DataMember]
        public string THESTATE
        {
            get { return thestate; }
            set { thestate = value; }
        }

        [DataMember]
        public string THEEMAIL
        {
            get { return theemail; }
            set { theemail = value; }
        }

        [DataMember]
        public string THEPASSWORD
        {
            get { return thepassword; }
            set { thepassword = value; }
        }
    }

    [DataContract]
    public class setPaymentInfo
    {
        string userName, credit, type, expirationDate, security;

        [DataMember]
        public string USERNAME
        {
            get { return userName; }
            set { userName = value; }
        }

        [DataMember]
        public string CREDITCARD
        {
            get { return credit; }
            set { credit = value; }
        }

        [DataMember]
        public string CARDTYPE
        {
            get { return type; }
            set { type = value; }
        }

        [DataMember]
        public string EXPIRATION
        {
            get { return expirationDate; }
            set { expirationDate = value; }
        }

        [DataMember]
        public string SECURITYCARD
        {
            get { return security; }
            set { security = value; }
        }
    }

    [DataContract]
    public class setPaymentInfoFlashlight
    {
        string userName, credit, type, expirationDate, security;

        [DataMember]
        public string USERNAME
        {
            get { return userName; }
            set { userName = value; }
        }

        [DataMember]
        public string CREDITCARD
        {
            get { return credit; }
            set { credit = value; }
        }

        [DataMember]
        public string CARDTYPE
        {
            get { return type; }
            set { type = value; }
        }

        [DataMember]
        public string EXPIRATION
        {
            get { return expirationDate; }
            set { expirationDate = value; }
        }

        [DataMember]
        public string SECURITYCARD
        {
            get { return security; }
            set { security = value; }
        }
    }

    [DataContract]
    public class insertCholorsHighScore
    {
        string userName, ispaidAccount, thecost;
        int highScore;

        [DataMember]
        public string USERNAME
        {
            get { return userName; }
            set { userName = value; }
        }

        [DataMember]
        public int HIGHSCORE
        {
            get { return highScore; }
            set { highScore = value; }
        }

        [DataMember]
        public string PAIDACCOUNT
        {
            get { return ispaidAccount; }
            set { ispaidAccount = value; }
        }

        [DataMember]
        public string COST
        {
            get { return thecost; }
            set { thecost = value; }
        }
    }

    [DataContract]
    public class insertFlashlightClick
    {
        string userName;
        int theClick, thepaidAccount;
        double theCost;

        [DataMember]
        public string USERNAME
        {
            get { return userName; }
            set { userName = value; }
        }

        [DataMember]
        public int CLICK
        {
            get { return theClick; }
            set { theClick = value; }
        }

        [DataMember]
        public int ISPAIDACCOUNT
        {
            get { return thepaidAccount; }
            set { thepaidAccount = value; }
        }

        [DataMember]
        public double COST
        {
            get { return theCost; }
            set { theCost = value; }
        }
    }
}
