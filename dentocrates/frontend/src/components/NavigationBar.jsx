import './NavigationBar.css'
import './Element.css'
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {email, role} from "./token/TokenDecoder";

function NavigationBar() {
    const navigate = useNavigate();
    const [logedInUserEmail, setLogedInUserEmail] = useState(null);
    const [isHidden,setIsHidden] = useState(true);

    const handleLogout = () => {
        localStorage.clear();
        setLogedInUserEmail(null);
        navigate("/");
    }

     useEffect(() => {
        if(email() && role()){
            setLogedInUserEmail(email());
        }
    },[email()]);

    return (
        <div className='navigationBar'>
            <img className='logo' src={process.env.PUBLIC_URL + '/dentocrates-light-logo.png'} alt="logo" />
            {logedInUserEmail === null &&
                <>
            <button id='tabs' onClick={() => navigate("/")}>Login</button>
            <button id='tabs' onClick={() => navigate("/register")}>Register</button>
                </>
            }
            <button id='tabs' onClick={() => navigate("/home")}>Home</button>
            {logedInUserEmail !== null &&
                <>
            <button id='tabs' onClick={() => navigate("/clinic")}>Search clinic</button>
            <button id='tabs' onClick={() => navigate("/dentist")}>Search dentist</button>
            <button id='tabs' onClick={() => navigate("/appointments")}>Appointments</button>
                </>
            }
            {logedInUserEmail !== null && role() === "DENTIST" &&
                <>
                    <button id='tabs' onClick={() => navigate("/clinic/register")}>Register clinic</button>
                    <button id='tabs' onClick={() => navigate("/location")}>Register location</button>
                </>
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
                <button className='logoutButton' onClick={() => navigate("/client")}>Profile</button>
                </div>
                </div>
            </div>
            :
            <></>}
        </div>
    )
}

export default NavigationBar;
