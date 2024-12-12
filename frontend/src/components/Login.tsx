import React, { useState } from "react";
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
                const token = data.token; // Assuming the token is returned as data.token
                localStorage.setItem("token", token); // Save the token for future requests

                try {
                    // Decode the token to get the userId
                    const decodedToken: any = jwtDecode(token);
                    const userId = decodedToken.userId; // Assuming userId is part of the decoded token

                    if (userId) {
                        localStorage.setItem("userId", userId);
                    } else {
                        throw new Error("userId is missing in the token.");
                    }
                } catch (decodeError) {
                    console.error("Error decoding JWT token:", decodeError);
                    setErrorMessage("Failed to decode token. Please try again.");
                    return;
                }

                setErrorMessage(""); // Clear error if successful
                alert("Login successful!");

                // Redirect to profile page or another page in the app
                window.location.href = "/"; // Adjust the route based on your app's flow
            } else {
                setErrorMessage("Invalid username or password. Please try again.");
            }
        } catch (error) {
            console.error("Login error:", error);
            setErrorMessage("An error occurred. Please try again.");
        }
    };

    // Inline-styling
    const styles = {
        container: {
            maxWidth: "400px",
            margin: "0 auto",
            padding: "20px",
            backgroundColor: "#f5f5f5",
            borderRadius: "8px",
            boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
        },
        header: {
            textAlign: "center" as "center",
            marginBottom: "20px",
        },
        formGroup: {
            marginBottom: "15px",
        },
        label: {
            display: "block",
            marginBottom: "5px",
            fontWeight: "bold" as "bold",
        },
        input: {
            width: "100%",
            padding: "10px",
            border: "1px solid #ccc",
            borderRadius: "4px",
            boxSizing: "border-box" as "border-box",
        },
        errorMessage: {
            color: "red",
            textAlign: "center" as "center",
            marginTop: "10px",
        },
        button: {
            width: "100%",
            padding: "10px",
            backgroundColor: "#4CAF50",
            color: "white",
            border: "none",
            borderRadius: "4px",
            cursor: "pointer",
        },
        buttonHover: {
            backgroundColor: "#45a049",
        },
    };

    return (
        <div style={styles.container}>
            <h2 style={styles.header}>Login</h2>
            <form onSubmit={handleSubmit}>
                <div style={styles.formGroup}>
                    <label htmlFor="username" style={styles.label}>
                        Username
                    </label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        style={styles.input}
                    />
                </div>
                <div style={styles.formGroup}>
                    <label htmlFor="password" style={styles.label}>
                        Password
                    </label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        style={styles.input}
                    />
                </div>
                {errorMessage && <p style={styles.errorMessage}>{errorMessage}</p>}
                <button type="submit" style={styles.button}>
                    Log In
                </button>
            </form>
        </div>
    );
};

export default Login;
