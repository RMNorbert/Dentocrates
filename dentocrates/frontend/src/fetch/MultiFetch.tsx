import { token } from "../components/token/TokenDecoder";


export async function MultiFetch<T>(url: string, method?: string, answerObject?: any): Promise<T> {
    try {
        const headers: Record<string, string> = token()
            ? {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token()}`,
            }
            : {
                "Content-Type": "application/json",
            };
        const response = await fetch((url[0] === "/" ? "" : "/") + url, {
            method: method ?? "GET",
            body: answerObject ? JSON.stringify(answerObject) : undefined,
            headers: headers,
        });

        if (!response.ok) {
            const errorMessage = `Failed to ${method ?? "GET"} to table: ${url}`;
            console.error(errorMessage);
            throw new Error(errorMessage);
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        throw error;
    }
}
