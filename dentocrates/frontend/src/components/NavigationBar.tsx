import './NavigationBar.css'
import './Element.css'
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {email, role} from "./token/TokenDecoder";
import React from 'react';

function NavigationBar() {
    const navigate = useNavigate();
    const [loggedInUserEmail, setLoggedInUserEmail] = useState<string | null>(null);
    const [isHidden, setIsHidden] = useState<boolean>(true);

    const handleLogout = () => {
        localStorage.clear();
        setLoggedInUserEmail(null);
        navigate('/');
    };

    useEffect(() => {
        if (email() && role()) {
            setLoggedInUserEmail(email());
        }
    }, [email()]);

    return (
        <div className="navigationBar">
            <img className="logo" src={process.env.PUBLIC_URL + '/dentocrates-light-logo.png'} alt="logo" />
            {loggedInUserEmail === null && (
                <>
                    <button id="tabs" onClick={() => navigate('/')}>
                        Login
                    </button>
                    <button id="tabs" onClick={() => navigate('/register')}>
                        Register
                    </button>
                </>
            )}
            <button id="tabs" onClick={() => navigate('/home')}>
                Home
            </button>
            {loggedInUserEmail !== null && (
                <>
                    <button id="tabs" onClick={() => navigate('/clinic')}>
                        Search clinic
                    </button>
                    <button id="tabs" onClick={() => navigate('/dentist')}>
                        Search dentist
                    </button>
                    <button id="tabs" onClick={() => navigate('/appointments')}>
                        Appointments
                    </button>
                </>
            )}
            {loggedInUserEmail !== null && role() === 'DENTIST' && (
                <>
                    <button id="tabs" onClick={() => navigate('/clinic/register')}>
                        Register clinic
                    </button>
                    <button id="tabs" onClick={() => navigate('/location')}>
                        Register location
                    </button>
                </>
            )}
            {loggedInUserEmail != null ? (
                <div className="logout">
                    <p
                        onMouseEnter={() => setIsHidden(!isHidden)}
                        style={{ display: !isHidden ? 'none' : 'block' }}
                    >
                        {loggedInUserEmail}
                    </p>
                    <div style={{ display: isHidden ? 'none' : 'block' }} onMouseLeave={() => setIsHidden(!isHidden)}>
                        <div className="box">
                            <button className="logoutButton" onClick={() => handleLogout()}>
                                Logout
                            </button>
                            <button className="logoutButton" onClick={() => navigate('/client')}>
                                Profile
                            </button>
                        </div>
                    </div>
                </div>
            ) : (
                <></>
            )}
        </div>
    );
}

export default NavigationBar;