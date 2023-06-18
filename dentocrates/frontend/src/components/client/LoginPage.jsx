import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const isMounted = useRef(true);
    const navigate = useNavigate()

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
            <h3 className="register"
                onClick={() =>  navigate("/register")}
            >
                Not registered yet?
            </h3>
        </div>
    );
}

export default LoginPage;

