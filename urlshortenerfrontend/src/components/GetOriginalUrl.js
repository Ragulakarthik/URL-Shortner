import React, { useState } from 'react';
import axios from 'axios';
import '../css/GetOriginalUrl.css'; // Import your CSS file for styling

const GetOriginalUrl = () => {
    const [shortUrl, setShortUrl] = useState('');
    const [originalUrl, setOriginalUrl] = useState('');
    const [error, setError] = useState('');

    const handleFetch = async (e) => {
        e.preventDefault();
        setError(''); // Reset any previous errors
        try {
            const response = await axios.get('http://localhost:8080/destination', {
                params: { shortUrl }
            });
            setOriginalUrl(response.data);
        } catch (error) {
            console.error("Error fetching original URL:", error);
            setError('Error fetching original URL: ' + (error.response?.data || error.message));
        }
    };

    return (
        <div className="get-original-url-container">
            <h2>Get Original URL</h2>
            <form onSubmit={handleFetch} className="url-form">
                <input 
                    type="text" 
                    value={shortUrl} 
                    onChange={(e) => setShortUrl(e.target.value)} 
                    placeholder="Enter Short URL" 
                    required 
                    className="url-input"
                />
                <button type="submit" className="submit-button">Get Original URL</button>
            </form>
            {originalUrl && <p className="original-url">Original URL: <a href={originalUrl} target="_blank" rel="noopener noreferrer">{originalUrl}</a></p>}
            {error && <p className="error-message">{error}</p>}
        </div>
    );
};

export default GetOriginalUrl;
