import { token } from "../components/token/TokenDecoder";
import { useState } from "react";

export const MultiFetch = () => {
    const [isLoading, setLoading] = useState(true);
    const data = async (url, method, answerObject) => {

        try {
            setLoading(true);
            const headers = token() ?
                {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token()}`,
                } :
                {
                    "Content-Type": "application/json",
                }
            const response = await fetch(url, {
                method: method ?? "GET",
                //credentials: 'include',
                body: answerObject ? JSON.stringify(answerObject) : undefined,
                headers: headers,
            });

            if (response.ok) {
                setLoading(false);
            } else {
                const errorMessage = `Failed to ${method ?? "GET"} to table: ${url}`;
                console.error(errorMessage);
                throw new Error(errorMessage);
            }

            const contentType = response.headers.get("content-type");
            if (contentType && contentType.includes("application/json")) {
                return await response.json();
            } else {
                return await response.text();
            }
        } catch (error) {
            console.error(error);
            throw error;
        } finally {
            setLoading(false);
        }

    };

    return { data, isLoading };
};
