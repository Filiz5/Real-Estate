'use server';
import { errorObject } from '@/helpers/functions/error-object';
import { config } from '@/helpers/config';


const API_URL = config.api.baseUrl;

export const getAdvertBySlug = async (slug) => {

  try {
    const response = await fetch(`${API_URL}/adverts/${slug}`);
    if (!response.ok) return errorObject('Failed to get the Advert by slug.');

    const data = await response.json();

   
    return data;
  } catch (error) {
    console.error('Error fetching adverts:', error);
    return errorObject('There was an error getting the Advert by slug.');
  }
};