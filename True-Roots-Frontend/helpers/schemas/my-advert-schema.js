import * as yup from 'yup';

export const advertSchema = yup.object().shape({
  title: yup.string().required('Title is required'),
  price: yup.number().required('Price is required').positive('Price must be positive'),
  image: yup.string().url('Image must be a valid URL').required('Image URL is required'),
  category: yup.string().required('Category is required'),
  description: yup.string().required('Description is required'),
  status: yup.string().oneOf(['pending', 'approved', 'rejected'], 'Invalid status').required('Status is required'),
  adress: yup.string().required('Adress is required'), 
});
