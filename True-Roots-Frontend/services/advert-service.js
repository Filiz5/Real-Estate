"use server";

import { getAuthHeader } from "@/helpers/auth-helper";

import { config } from '@/helpers/config';
import { errorObject } from "@/helpers/functions/error-object";
import { wait } from "@/utils/wait";

const API_URL = config.api.baseUrl; 

// api çağrısında Authorization header'ı ekliyoruz
export const getAdvertById = async (id) => {
  const response = await fetch(`${API_URL}/adverts/${id}/auth`, {
    method: 'GET',
    headers: await getAuthHeader()
  });

  if (!response.ok) {
    throw new Error(`Error while fetching advert `);
  }

  const data = await response.json();

  return  data;
};


// Tüm ilanları getir
export const getAdvertAll = async (
  page = 0,
  size = 10,
  sort = 'createdAt',
  type = 'asc',
  searchTerm
) => {
  const qs = `q=${searchTerm}&page=${page}&size=${size}&sort=${sort}&type=${type}`;
  try {
    const response = await fetch(`${API_URL}/adverts/auth?${qs}`, {
      method: 'GET',
      headers: await getAuthHeader()
    });
    if (!response.ok) {
      throw new Error(`Failed to fetch adverts: ${response.statusText}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching all adverts:', error.message);
    throw error;
  }
};

export const editAdvert= async (advertId, data) => {
  const response = await fetch(`${API_URL}/${advertId}`, {
    method: "PUT",
    headers: await getAuthHeader(),
    body: JSON.stringify(data),
  });
  return response;
}


export const createAdvert = async (payload) => {
  const response = await fetch(`${API_URL}/adverts`, {
    method: 'POST',
    headers: await getAuthHeader(),
    body: JSON.stringify(payload)
  });
  if (!response.ok) {
    return { ok: false, message: 'Failed to create advert' };
  }
  const data = await response.json();
  return { ok: true, message: data.message };
}

export const uploadImage = async (payload, advertId) => {
  const response = await fetch(`${API_URL}/images/${advertId}/upload`, {
    method: 'PUT',
    headers: await getAuthHeader(),
    body: JSON.stringify(payload)
  });
  if (!response.ok) {
    return { success: false, message: 'Failed to upload image' };
  }
  const data = await response.json();
  return { success: true, message: data.message };
}

export const deleteImage = async (imageId) => {
  const response = await fetch(`${API_URL}/images/${imageId}`, {
    method: 'DELETE',
    headers: await getAuthHeader()
  });
  if (!response.ok) {
    return { success: false, message: 'Failed to delete image' };
  }
  const data = await response.json();
  return { success: true, message: data.message };
}

export const updateFesturedImage = async (imageId) => {
  const response = await fetch(`${API_URL}/images/${imageId}`, {
    method: 'PUT',
    headers: await getAuthHeader()
  });
  if (!response.ok) {
    return { success: false, message: 'Failed to set as featured' };
  }
  const data = await response.json();
  return { success: true, message: data.message };
}

export const updateAdvert = async (payload, id) => {
  const response = await fetch(`${API_URL}/adverts/auth/${id}`, {
    method: 'PUT',
    headers: await getAuthHeader(),
    body: JSON.stringify(payload)
  });
  if (!response.ok) {
    return { ok: false, message: 'Failed to create advert' };
  }
  const data = await response.json();
  return { ok: true, message: data.message };
};

export const getAdvertsAdmin = async (
    searchTerm,
    categoryId,
    advertType,
    status,
    priceStart,
    priceEnd,
    currentPage = 0,
    pageSize=10,
    sort='createdAt',
    type='asc',) => {
      const qs = `q=${searchTerm}&categoryId=${categoryId}&advertType=${advertType}&status=${status}&priceStart=${priceStart}&priceEnd=${priceEnd}&page=${currentPage}&size=${pageSize}&sort=${sort}&type=${type}`;
  try {
    const response = await fetch(`${API_URL}/adverts/admin?${qs}`, {
      method: 'GET',
      headers: await getAuthHeader()
    });
    if (!response.ok) {
      throw new Error(`Failed to fetch adverts`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching all adverts:', error.message);
    throw error;
  }
}

export const deleteAdvert = async (id) => {
  try{
    const response = await fetch(`${API_URL}/adverts/admin/${id}`, {
      method: 'DELETE',
      headers: await getAuthHeader()
    });
    if (!response.ok) {
      return { success: false, message: 'Failed to delete advert' };
    }
    const data = await response.json();
    return { success: true, message: data.message };
  }catch(error){
    throw new Error('Something went wrong while deleting the advert. Please try again later.');
  }
}

export const getPopularAdverts = async () => {

  try{
    const res = await fetch(`${API_URL}/adverts/popular?amount=6`, {
      method: 'GET',
      headers: 
        {
          'Content-Type': 'application/json',
        }
    });

    const data = await res.json();
    if(!res.ok){
      throw new Error('Failed to fetch popular adverts');
    }

    return data;

  }catch(error){
    throw error;
  }
};


export const getAdvertByIdAdmin = async (id) => {

  try{
    const res = await fetch(`${API_URL}/adverts/${id}/admin`, {
      method: 'GET',
      headers: await getAuthHeader()
    });

    const data = await res.json();
    if(!res.ok){
      return errorObject('Failed to fetch advert');
    }

    return data;

  }catch(error){
    return errorObject('There was an error while fetching advert');
  }
};

export const updateStatus = async (id, payload) => {

  try{
    const res = await fetch(`${API_URL}/adverts/admin/${id}/statusUpdate`, {
      method: 'PUT',
      headers: await getAuthHeader(),
      body: JSON.stringify(payload)
    });
    await wait(2000);
    const data = await res.json();
    if(!res.ok){
      throw new Error(data.message || 'Failed to update status');
    }

    return data.message || 'Status updated successfully';
  }catch(error){
    throw new Error( 'Connection error occurred while updating the status. Please try again later.');
  }
};



export const deleteAdvertAuthUser = async (id) => {
  try{
    const response = await fetch(`${API_URL}/adverts/auth/${id}`, {
    method: 'DELETE',
    headers: await getAuthHeader()
  });
  if (!response.ok) {
    return { success: false, message: 'Failed to delete advert' };
  }
  const data = await response.json();
  return { success: true, message: data.message };
  }catch(error){
    throw new Error('Something went wrong while deleting the advert. Please try again later.');
  }
};
