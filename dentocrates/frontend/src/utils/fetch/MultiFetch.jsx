import { token } from "../token/TokenDecoder";

export const MultiFetch = () => {
    const data = async (url, method, answerObject) => {
        try {
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
                body: answerObject ? JSON.stringify(answerObject) : undefined,
                headers: headers,
            });
            if (!response.ok)  {
                const errorMessage = `Failed to ${method ?? "GET"} to table: ${url}`;
                console.error(errorMessage);
                return await response.text();
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
        }
    };
    return { data };
};
