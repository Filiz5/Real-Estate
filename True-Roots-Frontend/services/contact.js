"use server";

import { getAuthHeader } from '@/helpers/auth-helper'; 
import Swal from 'sweetalert2'; 
import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl;




export const getContactMessagesFromDatabase = async (page = 0, size = 500) => {
  try {
    const headers = await getAuthHeader();
    const response = await fetch(
      `${API_URL}/contact-messages/get?page=${page}&size=${size}&sort=createdAt&type=asc`,
      { method: 'GET', headers: headers }
    );

    if (!response.ok) {
      throw new Error('Failed to fetch data.');
    }

    const data = await response.json();
    return { content: data.content, totalPages: data.totalPages };
  } catch (error) {
    console.error('Error fetching data:', error);
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Failed to fetch messages. Please try again.',
    });
    throw error;
  }
};


export const getContactMessageById = async (id) => {
  try {
    const headers = await getAuthHeader();
    const response = await fetch(
      `${API_URL}/contact-messages/getContactMessageById/${id}`,
      { method: 'GET', headers: headers }
    );
    

    if (!response.ok) {
      throw new Error('Failed to fetch the message.');
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching message:', error);
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Failed to load message details. Please try again.',
    });
    throw error;
  }
};


export const deleteContactMessage = async (id) => {
  try {
    const headers = await getAuthHeader();
    const response = await fetch(
      `${API_URL}/contact-messages/deleteContactMessageById/${id}`,
      { method: 'DELETE', headers: headers }
    );

    if (!response.ok) {
      const errorText = await response.text();
      console.error('Error details:', errorText);
      throw new Error('Deletion failed.');
    }

    Swal.fire({
      icon: 'success',
      title: 'Deleted!',
      text: 'The message has been successfully deleted.',
    });
  } catch (error) {
    console.error('Deletion error:', error);
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Failed to delete the message. Please try again.',
    });
    throw error;
  }
};


export const updateMessageReadStatus = async (id, isRead) => {
  const url = isRead
    ? `${API_URL}/contact-messages/readMessageById/${id}`
    : `${API_URL}/contact-messages/unreadMessageById/${id}`;
  try {
    
    const headers = await getAuthHeader();
    
    const response = await fetch(url, {
      method: "PATCH",
      headers: headers,  
    });
    if (!response.ok) {
      const errorData = await response.json();
      console.error('Error response:', errorData);
      throw new Error(`Failed to update message read status: ${errorData.message}`);
    }
  } catch (error) {
    console.error("Request failed:", error);
    throw error;
  }
};
