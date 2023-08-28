import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const isMounted = useRef(true);
    const navigate = useNavigate();

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

    const handleSubmit = async (event) => {
        event.preventDefault();

        const response =
            await fetch('/api/authenticate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                }),
            });

        if (response.ok) {
            const data = await response.json();
            const token = data.token;
            const id = data.id;
            localStorage.setItem('userId', id);
            localStorage.setItem('token', token);
            navigate("/home");
        }

    }

    const handleGoogleLogin = async () => {
        try {
            const response = await fetch("/api/oauth2/authorizationPageUrl/google");
            if (response.ok) {
                const url = await response.text();
                window.location.replace(url);
            } else {
                console.error("Failed to fetch Google authorization URL");
            }
        } catch (error) {
            console.error("Error fetching Google authorization URL:", error);
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
                <button type="submit">Login</button>
            </form>
            <button onClick={handleGoogleLogin}>Sign in With Google</button>
            <h3 className="register"
                onClick={() =>  navigate("/register")}
            >
                Not registered yet?
            </h3>
        </div>
    );
}

export default LoginPage;

