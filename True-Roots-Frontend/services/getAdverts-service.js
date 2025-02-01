'use server';
import { errorObject } from '@/helpers/functions/error-object';
import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl;

export const getAdvertsWithSearchParameter = async (
  page = 0,
  size = 10,
  sort = 'createdAt',
  type = 'asc',
  search,
  categoryId,
  priceStart,
  priceEnd,
  status,
  location
) => {
  const qs = new URLSearchParams({
    page,
    size,
    sort,
    type,
    q: search || '',
    categoryId: categoryId || '',
    advertType: status || '',
    priceStart: priceStart || '',
    priceEnd: priceEnd || '',
    location: location || ''
  });
//http://localhost:3000/properties?advertType=1&page=0&size=10&sort=createdAt&type=asc
//http://localhost:3000/properties?page=0&size=10&sort=createdAt&type=asc&status=1&priceStart=
  

  try {
    const response = await fetch(`${API_URL}/adverts?${qs}`);
    if (!response.ok) return errorObject('Failed to get the Adverts.');

    const data = await response.json();

    if (location) {
      const filteredContent = data?.object?.content?.filter(
        (advert) => Number(advert.city.id) === Number(location)
      );

      // Return filtered data
      return {
        ...data,
        object: {
          ...data.object,
          content: filteredContent
        }
      };
    }

    // Return original data if no location filtering is applied
    return data;
  } catch (error) {
    console.error('Error fetching adverts:', error);
    return errorObject('There was an error getting the Adverts.');
  }
};