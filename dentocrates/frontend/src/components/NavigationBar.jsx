import './NavigationBar.css'
import './Elements.css'
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function NavigationBar() {
    const navigate = useNavigate();
    const [logedInUserEmail, setLogedInUserEmail] = useState(null);
    const [userRole , setUserRole] = useState(null);
    let url = window.location.href.split('/');
    let currentUrl = url[url.length-1];

    const handleLogout = () => {
        localStorage.clear();
        setUserRole(null);
        setLogedInUserEmail(null);
        navigate("/login");
    }

     useEffect(() => {
        if(email() && role()){
            setLogedInUserEmail(email());
            setUserRole(role());
        }
    },[email()]);

    return (
        <div className='navigationBar'>
            <img className='logo' src={process.env.PUBLIC_URL + '/dentocrates-light-logo.png'} alt="logo" />
            <div id='tabs'>Home</div>
            <div id='tabs'>Search clinic</div>
            <div id='tabs'>Search dentist</div>
            <div id='tabs'>Appointments</div>
            <div id='tabs'>Profile</div>
            <div id='tabs'>Logout</div>
            {userRole && userRole === "DENTIST" ?
      <h2 className='clinicLabel'>Clinics</h2>
      :
                userRole && userRole === "CUSTOMER" ?
      <h2 className='searchLabel'>Search</h2>
      : <></>
      }
      {logedInUserEmail != null ? <div className='logout'
      >
                <p
                  onMouseEnter={() => setIsHidden(!isHidden)}
                  style={{ display: !isHidden ? "none" : "block" }}
                  >{logedInUserEmail}</p>
                <div style={{ display: isHidden ? "none" : "block" }}
                onMouseLeave={() => setIsHidden(!isHidden)}
                >
                <div className='box' >
                <button className='logoutButton'
                onClick={() => handleLogout()}
                >
                  Logout
                </button>
                {userRole && userRole === "CUSTOMER" &&
                <button className='logoutButton'
                onClick={() => navigateTo("/pass")}
                >
                  Pass
                </button>}
                </div>
                </div>
            </div>
            :
            <></>}
        </div>
    )
}

export default NavigationBar;
