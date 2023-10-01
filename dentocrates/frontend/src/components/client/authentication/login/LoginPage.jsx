import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {Reset} from "../reset-password/Reset";
import {MultiFetch} from "../../../../fetch/MultiFetch";
import { handleGoogleLogin } from "./OauthLogin";

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [authenticationCode, setAuthenticationCode] = useState('');
    const [clientIsValid, setClientIsValid] = useState(false);
    const [isResetPasswordRequested, setIsResetPasswordRequested] = useState(false);
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
        const authenticationRequestUrl = '/java-backend/api/request/authenticate';
        const requestBody = {email: email};
        const response = await data(authenticationRequestUrl, "POST", requestBody );
        if (response === true) {
            setClientIsValid(true);
        }
    }
    const handleSubmit = async (event) => {
        event.preventDefault();
        const authenticationUrl = '/java-backend/api/authenticate';
        const requestBody = {
            email: email,
            password: password,
            role:"",
            authenticationCode: authenticationCode
        };

        const response = await data(authenticationUrl,'POST',  requestBody);

        if (response.id) {
            const token = response.token;
            const id = response.id;
            localStorage.setItem('userId', id);
            localStorage.setItem('token', token);
            navigate("/home");
        }

    }

    return (
        <div className='pageContent'>
            <h1>Login</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="email">Email:</label>
                    <input type="text" id="email" value={email} onChange={handleEmailChange} />
                </div>
                <div>
                    <label htmlFor="password">Password:</label>
                    <input type="password" id="password" value={password} onChange={handlePasswordChange} />
                </div>
                {clientIsValid ?
                    <><label htmlFor="code">Type sent Authentication Code:</label>
                        <input
                        type="text"
                        id="code"
                        value={authenticationCode}
                        onChange={handleAuthenticationCodeChange}
                    />
                    <button type="submit">Login</button></> :
                    <button
                        type="button"
                        onClick={handleAuthenticationRequest}
                    >
                        Login
                    </button>
                }
            </form>
            <button onClick={handleGoogleLogin}>Sign in With Google</button>
            <h3 className="register"
                onClick={() =>  navigate("/register")}
            >
                Not registered yet?
            </h3>
            <h3 className="register"
                onClick={() =>  setIsResetPasswordRequested(true)}
            >
                Forgot your password ?
            </h3>
            {isResetPasswordRequested ?
                <div>
                    <Reset/>
                </div>
            :
                <></>
            }
        </div>
    );
}

export default LoginPage;

