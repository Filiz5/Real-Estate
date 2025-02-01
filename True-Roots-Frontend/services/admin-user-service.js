"use server";

import { getAuthHeader } from "@/helpers/auth-helper";  
import { config } from "@/helpers/config";


const API_URL = config.api.baseUrl;


 
export const fetchUsers = async (page) => {
  try {
    const response = await fetch(`${API_URL}/users/admin?q=&page=${page}&size=10&sort=createdAt&type=desc`, {
      method: 'GET',
      headers: await getAuthHeader(), 
    });

    if (!response.ok) {
      throw new Error('Failed to fetch users');
    }

    return response.json(); 
  } catch (error) {
    console.error('Error fetching users:', error);
    throw error; 
  }
};


export const fetchUser = async (userId) => {
  try {
    const response = await fetch(`${API_URL}/users/${userId}/admin`, {
      method: 'GET',
      headers: await getAuthHeader(),
    });

    if (!response.ok) {
      const errorData = await response.text(); // Yanıtı metin olarak al
      console.error("API Error Response (Raw):", errorData); // Ham yanıtı göster
      throw new Error("Kullanıcı verisi alınamadı: " + errorData);
    }

    const data = await response.json();

    if (!data || typeof data !== "object" || !data.object) {
      throw new Error("Invalid response structure: " + JSON.stringify(data));
    }

    return data;
  } catch (error) {
    console.error('Error fetching user:', error);
    throw error;
  }
};








