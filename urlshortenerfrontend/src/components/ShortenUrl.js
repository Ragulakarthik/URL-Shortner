import React, { useState } from 'react';
import axios from 'axios';
import '../css/ShortenUrl.css'; // Updated import path for the CSS file

const ShortenUrl = () => {
    const [originalUrl, setOriginalUrl] = useState('');
    const [shortUrl, setShortUrl] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(''); // Reset any previous errors
        try {
            const response = await axios.post('http://localhost:8080/shorten', null, {
                params: { originalUrl }
            });
            setShortUrl(response.data);
        } catch (error) {
            console.error("Error shortening URL:", error);
            setError('Error shortening URL: ' + (error.response?.data || error.message));
        }
    };

    return (
        <div className="shorten-url-container">
            <h2>Shorten Your URL</h2>
            <form onSubmit={handleSubmit} className="url-form">
                <input 
                    type="text" 
                    value={originalUrl} 
                    onChange={(e) => setOriginalUrl(e.target.value)} 
                    placeholder="Enter URL to shorten" 
                    required 
                    className="url-input"
                />
                <button type="submit" className="submit-button">Shorten</button>
            </form>
            {shortUrl && <p className="shortened-url">Shortened URL: <a href={shortUrl} target="_blank" rel="noopener noreferrer">{shortUrl}</a></p>}
            {error && <p className="error-message">{error}</p>}
        </div>
    );
};

export default ShortenUrl;
