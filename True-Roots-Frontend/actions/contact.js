"use server";

import { config } from "@/helpers/config";

const API_URL = config.api.baseUrl; 

export const submitContactForm = async (formData) => {
  try {
      const response = await fetch(`${API_URL}/contact-messages/create`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
      });
     

    if (!response.ok) {
      throw new Error('Form gönderimi başarısız oldu.');
    }

    return await response.json();
  } catch (error) {
    console.error('Form gönderim hatası:', error);
    throw error;
  }
};