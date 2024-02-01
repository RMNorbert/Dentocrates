import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {Reset} from "../reset-password/Reset";
import {MultiFetch} from "../../../../utils/fetch/MultiFetch";
import { handleGoogleLogin } from "../oauth/OauthLogin";

function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [authenticationCode, setAuthenticationCode] = useState("");
    const [clientIsValid, setClientIsValid] = useState(false);
    const [isResetPasswordRequested, setIsResetPasswordRequested] = useState(false);
    const [message, setMessage] = useState("");
    const [messageIsHidden, setMessageIsHidden] = useState(true);
    const isMounted = useRef(true);
    const navigate = useNavigate();
    const { data } = MultiFetch();

    useEffect(() => {
        return () => {
            isMounted.current = false;
        };
    }, []);
    const handleEmailChange = (event) => {
        setEmail(event.target.value);
    };
    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };
    const handleAuthenticationCodeChange = (event) => {
        setAuthenticationCode(event.target.value);
    }
    const handleAuthenticationRequest = async () => {
        try{
            const authenticationRequestUrl = "/api/request/authenticate";
            const requestBody = {email: email, password: password};
            const response = await data(authenticationRequestUrl, "POST", requestBody );
            if (response === true) {
                setClientIsValid(true);
            }
            else if(response.includes("Proxy")) {
                setMessage("Could not make request, try again later")
                setMessageIsHidden(false);
            }
            else {
                setMessage(response)
                setMessageIsHidden(false);
            }
        } catch (e) {
            console.error(e);
        }
    }
    const handleSubmit = async (event) => {
        try {
            event.preventDefault();
            const authenticationUrl = "/api/authenticate";
            const requestBody = {
                email: email,
                password: password,
                role:"",
                authenticationCode: authenticationCode
            };

            const response = await data(authenticationUrl,"POST",  requestBody);

            if (response.id) {
                const token = response.token;
                const id = response.id;
                localStorage.setItem('userId', id);
                localStorage.setItem('token', token);
                navigate("/home");
            }
        } catch (e) {
            console.error(e.message);
            setMessageIsHidden(false);
            setMessage("Invalid email or password")
        }
    }

    return (
        <div className="pageContent shadowBorder roundBox">
            <div className="shadowBorder">
            <h1 className="register-title">Login</h1>
                <p display={messageIsHidden ? "none": "none"}>{message}</p>
            <form onSubmit={handleSubmit}>
                <div className="inputBox">
                    <label htmlFor="email">Email:</label>
                    <input
                        type="text"
                        id="email"
                        value={email}
                        onChange={handleEmailChange}
                    />
                </div>
                <div className="inputBox">
                    <label htmlFor="password">Password:</label>
                    <input
                        className="shadowBorder"
                        type="password"
                        id="password"
                        value={password}
                        onChange={handlePasswordChange}
                    />
                </div>
                {clientIsValid ?
                    <div className="inputBox">
                        <label htmlFor="code">
                            Type sent Authentication Code:
                        </label>
                        <input
                        type="text"
                        id="code"
                        value={authenticationCode}
                        onChange={handleAuthenticationCodeChange}
                    />
                    <button className="inputBox" type="submit">
                        Login</button>
                    </div> :
                    <button
                        className="inputBox shadowBorder"
                        type="button"
                        onClick={() => handleAuthenticationRequest()}
                    >
                        Login
                    </button>
                }
            </form>
            </div>
            <div className="inputBox">
            <button className="shadowBorder" onClick={() => handleGoogleLogin()}>Sign in With Google</button>
            <button className="register shadowBorder"
                onClick={() =>  navigate("/register")}
            >
                Not registered yet?
            </button>
            <button className="register shadowBorder"
                onClick={() =>  setIsResetPasswordRequested(true)}
            >
                Forgot your password ?
            </button>
            {isResetPasswordRequested ?
                <div >
                    <Reset/>
                </div>
            :
                <></>
            }
            </div>
        </div>
    );
}

export default LoginPage;

