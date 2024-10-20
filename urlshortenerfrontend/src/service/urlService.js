// src/services/urlService.js

import axios from 'axios';

const BASE_URL = 'http://localhost:8080'; // Change this if your backend URL is different

const urlService = {
    // Method to shorten a URL
    shortenUrl: async (originalUrl) => {
        try {
            const response = await axios.post(`${BASE_URL}/shorten`, null, {
                params: { originalUrl },
            });
            return response.data; // Returns the shortened URL
        } catch (error) {
            console.error('Error shortening URL:', error);
            throw error; // Rethrow the error to handle it in the component
        }
    },

    // Method to get the original URL from a shortened URL
    getOriginalUrl: async (shortUrl) => {
        try {
            const response = await axios.get(`${BASE_URL}/destination`, {
                params: { shortUrl },
            });
            return response.data; // Returns the original URL
        } catch (error) {
            console.error('Error fetching original URL:', error);
            throw error;
        }
    },

    // Method to update a shortened URL
    updateShortUrl: async (shortUrl, newOriginalUrl) => {
        try {
            const response = await axios.put(`${BASE_URL}/update`, null, {
                params: { shortUrl, newOriginalUrl },
            });
            return response.data; // Returns true or false based on the success of the operation
        } catch (error) {
            console.error('Error updating shortened URL:', error);
            throw error;
        }
    },

    // Method to update the expiry of a shortened URL
    updateExpiry: async (shortUrl, daysToAdd) => {
        try {
            const response = await axios.put(`${BASE_URL}/updateExpiry`, null, {
                params: { shortUrl, daysToAdd },
            });
            return response.data; // Returns true or false based on the success of the operation
        } catch (error) {
            console.error('Error updating expiry:', error);
            throw error;
        }
    },
};

export default urlService;
