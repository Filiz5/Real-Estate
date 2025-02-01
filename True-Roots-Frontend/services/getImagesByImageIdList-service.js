'use server';
import { errorObject } from '@/helpers/functions/error-object';
import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl;

export const getImagesByImageIdList = async ({ imageIdList}) => {
  try {
    const response = await fetch(`${API_URL}/images?imageIdList=${imageIdList}`);

    if (!response.ok) return errorObject('Failed to get the advert images.');

    const data = await response.json();

    return data;
  } catch (error) {
    return errorObject('There was an error getting the advert images.');
  }
};
