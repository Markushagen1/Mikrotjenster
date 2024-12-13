import React, { useState } from 'react';
import {jwtDecode} from "jwt-decode";

const Login: React.FC = () => {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [errorMessage, setErrorMessage] = useState<string>("");

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        try {
            const response = await fetch("http://localhost:8083/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ username, password }),
            });

            if (response.ok) {
                const data = await response.json();
                const token = data.token;
                localStorage.setItem("token", token);

                const decodedToken: any = jwtDecode(token);
                const userId = decodedToken?.userId;

                if (!userId) {
                    throw new Error("userId is missing in the token.");
                }

                localStorage.setItem("userId", userId.toString());
                setErrorMessage("");
                alert("Login successful!");
                window.location.href = "/";
            } else {
                setErrorMessage("Invalid username or password. Please try again.");
            }
        } catch (error) {
            console.error("Login error:", error);
            setErrorMessage("An error occurred. Please try again.");
        }
    };

    return (
        <div className="flex flex-col items-center min-h-screen bg-gradient-to-b from-purple-50 via-white to-purple-100 p-8">
            <div className="max-w-md w-full bg-white shadow-md rounded-lg p-8">
                <h2 className="text-2xl font-bold mb-6 text-purple-900">Login</h2>
                <form onSubmit={handleSubmit}>
                    <label className="block mb-4">
                        <span className="text-gray-700">Username</span>
                        <input
                            type="text"
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-purple-500"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </label>
                    <label className="block mb-4">
                        <span className="text-gray-700">Password</span>
                        <input
                            type="password"
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-purple-500"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </label>
                    {errorMessage && <p className="text-red-500">{errorMessage}</p>}
                    <button
                        type="submit"
                        className="w-full py-2 px-4 mt-4 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-all"
                    >
                        Log In
                    </button>
                </form>
            </div>
        </div>
    );
};

export default Login;
