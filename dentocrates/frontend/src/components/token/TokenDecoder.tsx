import jwt_decode from 'jwt-decode';

export const token = () => {
    try{
        return localStorage.getItem('token');
    } catch (error) {
        return null;
    }
}
export const email = (): string | null => {
    try{
    const token = localStorage.getItem('token');
    if(token) {
        const decodedToken = jwt_decode<{ sub: string }>(token);
        return decodedToken.sub;
    }
    return null;
    } catch (error) {
        return null;
    }
}

export const role = (): string | null => {
    try {
        const token = localStorage.getItem('token');
        if(token) {
            const decodedToken = jwt_decode<{role:string}>(token);
            return decodedToken.role;
        }
        return null;
    } catch (error) {
          return null;
      }

}

export const userId = (): number | null => {
    try {
        const userIdString = localStorage.getItem('userId');
        if (userIdString) {
            return parseInt(userIdString, 10); // Parse string into a base-10 integer
        }
        return null;
    } catch (error) {
        return null;
    }
}
