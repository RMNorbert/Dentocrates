import './NavigationBar.css'
import '../Element.css'
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {email, role} from "../../utils/token/TokenDecoder";

function NavigationBar() {
    const navigate = useNavigate();
    const [loggedInUserEmail, setLoggedInUserEmail] = useState(null);
    const [isHidden,setIsHidden] = useState(true);

    const handleLogout = () => {
        localStorage.clear();
        setLoggedInUserEmail(null);
        navigate("/");
    }

     useEffect(() => {
        if(email() && role()){
            setLoggedInUserEmail(email());
        }
    },[email()]);

    return (
        <div className='navigationBar'>
            <img
                className='logo'
                src={process.env.PUBLIC_URL + '/dentocrates-light-logo.png'}
                alt="logo"
                onClick={() => navigate("/home")}
            />
            {loggedInUserEmail === null &&
                <>
            <button id='tabs' onClick={() => navigate("/")}>Login</button>
            <button id='tabs' onClick={() => navigate("/register")}>Register</button>
                </>
            }
            <button id='tabs' onClick={() => navigate("/home")}>Home</button>
            {loggedInUserEmail !== null &&
                <>
            <button id='tabs' onClick={() => navigate("/clinic")}>Search clinic</button>
            <button id='tabs' onClick={() => navigate("/dentist")}>Search dentist</button>
            <button id='tabs' onClick={() => navigate("/appointments")}>Appointments</button>
            <button id='tabs' onClick={() => navigate("/map")}>Map</button>
                </>
            }
            {loggedInUserEmail !== null && role() === "DENTIST" &&
                <>
                    <button id='tabs' onClick={() => navigate("/clinic/register")}>Register clinic</button>
                    <button id='tabs' onClick={() => navigate("/location")}>Register location</button>
                </>
            }
      {loggedInUserEmail != null ? <div className='logout'  style={role() === "DENTIST" ? { left: "14%" } : { left: "34%" }}
      >
                <p
                  onMouseEnter={() => setIsHidden(!isHidden)}
                  style={{ display: !isHidden ? "none" : "block" }}
                  >{loggedInUserEmail}</p>
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
