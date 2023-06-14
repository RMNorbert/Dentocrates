import './NavigationBar.css'
import './Element.css'
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {email, role} from "./token/TokenDecoder";
function NavigationBar() {
    const navigate = useNavigate();
    const [logedInUserEmail, setLogedInUserEmail] = useState(null);
    const [userRole , setUserRole] = useState(null);
    const [isHidden,setIsHidden] = useState(true);
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
            <button id='tabs'>Home</button>
            <button id='tabs'>Search clinic</button>
            <button id='tabs'>Search dentist</button>
            <button id='tabs'>Appointments</button>
            <button id='tabs'>Profile</button>
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
                onClick={() => navigate("/pass")}
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
