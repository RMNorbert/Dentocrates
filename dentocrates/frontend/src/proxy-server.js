const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();

// Proxy requests to the Java backend
app.use('/java-backend', createProxyMiddleware({
    target: 'http://localhost:8080/',
    changeOrigin: true,
    pathRewrite: {
        '^/java-backend': '', // Remove the /java-backend part from the URL
    },
}));

// Proxy requests to the Python web service
app.use('/python-service', createProxyMiddleware({
    target: 'http://127.0.0.1:5000/',
    changeOrigin: true,
    pathRewrite: {
        '^/python-service': '',
    },
}));

const port = process.env.PORT || 3001;

app.listen(port, () => {
    console.log(`Proxy server is running on port ${port}`);
});
