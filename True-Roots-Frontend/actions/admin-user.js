"use server";

import { getAuthHeader } from "@/helpers/auth-helper";  
import { config } from "@/helpers/config";


const API_URL = config.api.baseUrl;
export const updateUser = async (userId, payload) => {
    try {
      const headers = await getAuthHeader();
      const response = await fetch(`${API_URL}/users/${userId}/admin`, {
        method: 'PUT',
        headers: {
          ...headers,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),  // Güncellenen veriyi JSON olarak gönder
      });
  
      if (!response.ok) {
        const errorText = await response.text();  // Yanıtı metin olarak al
        console.error('Error response:', errorText);  // Konsola hata yazdır
        throw new Error(`Kullanıcı güncellenemedi: ${response.statusText}`);
      }
  
      return await response.json();  // Yanıtı döndür
    } catch (error) {
      console.error('Error updating user:', error);
      throw error;  // Hata fırlat
    }
  };

export const deleteUser = async (userId) => {
    try {
      const headers = await getAuthHeader();
  
      const response = await fetch(`${API_URL}/users/${userId}/admin`, {
        method: 'DELETE',
        headers,
      });
  
      if (!response.ok) {
        throw new Error(`Failed to delete user: ${response.statusText}`);
      }
  
      return await response.json();
    } catch (error) {
      console.error('Error deleting user:', error);
      throw error;
    }
  };


  //favorites

  export const deleteFavorite = async (favoriteId) => {
    try {
      const headers = await getAuthHeader();
      const response = await fetch(`${API_URL}/favorites/${favoriteId}/admin`, {
        method: 'DELETE',
        headers,
      });
  }
  catch (error) {
    console.error('Error deleting favorite:', error);
    throw error;
  }
};

//tour requests
export const deleteTourRequest = async (id) => {
  try {
    const headers = await getAuthHeader();
    const response = await fetch(`${API_URL}/tour-requests/${id}`, {
      method: 'DELETE',
      headers,
    });

    if (!response.ok) {
      const errorDetails = await response.text(); // Yanıtın detaylarını alın
      console.error(`API error: ${response.status} ${response.statusText} - ${errorDetails}`);
      throw new Error(`API error: ${response.status} ${response.statusText} - ${errorDetails}`);
    }

    // Yanıtın JSON olup olmadığını kontrol edin
    const contentType = response.headers.get("content-type");
    if (contentType && contentType.indexOf("application/json") !== -1) {
      return response.json();
    } else {
      return response.text();
    }
  } catch (error) {
    console.error('Error deleting tour request:', error);
    throw error;
  }
};

//adverts
export const deleteAdvert = async (id) => {
  try {
    const headers = await getAuthHeader();
    const response = await fetch(`${API_URL}/adverts/admin/${id}`, {
      method: 'DELETE',
      headers,
    });
  }

catch (error) {
  console.error('Error deleting advert:', error);
  throw error;
}
};



  

