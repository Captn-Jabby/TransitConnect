import axios from "axios";

const API_URL = "http://localhost:8080/auth";

export const login = async (username, password) => {
      try {
            const response = await axios.post(`${API_URL}/login`, {
                  username,
                  password,
            });
            if (response.data.token) {
                  localStorage.setItem("token", response.data.token);
                  localStorage.setItem("role", response.data.role);
            }
            return response.data;
      } catch (error) {
            throw error;
      }
};

export const register = async (username, email, password) => {
      try {
            const response = await axios.post(`${API_URL}/register`, {
                  username,
                  email,
                  password,
            });
            return response.data;
      } catch (error) {
            throw error;
      }
};

export const logout = () => {
      localStorage.removeItem("token");
      localStorage.removeItem("role");
};

export const getToken = () => {
      return localStorage.getItem("token");
};

export const isAuthenticated = () => {
      return !!localStorage.getItem("token");
};
