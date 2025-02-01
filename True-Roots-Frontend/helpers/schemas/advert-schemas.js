import * as yup from 'yup';

export const CreateAdvertSchema = yup.object().shape({
  title: yup
    .string()
    .min(3, 'Title must be at least 3 characters')
    .required('Title is required'),
  desc: yup
    .string()
    .min(10, 'Description must be at least 10 characters')
    .required('Description is required'),
  price: yup
    .number()
    .positive('Price must be a positive number')
    .typeError('Price must be a number')
    .required('Price is required'),
  advert_type_id: yup
    .number()
    .typeError('You have to choose an advert type')
    .required('Advert type is required'),
  category_id: yup
    .number()
    .typeError('You have to choose a category')
    .required('Category is required'),
  location: yup
    .string()
    .min(10, 'Please enter a valid address')
    .required('Address is required'),
  district_id: yup
    .number()
    .typeError('You have to choose an district')
    .required('District is required'),
  city_id: yup
    .number()
    .typeError('You have to choose an city')
    .required('City is required'),
  country_id: yup
    .number()
    .typeError('You have to choose an country')
    .required('Country is required'),
  images: yup
    .array()
    .of(
      yup.object().shape({
        data: yup.string().required(),
        type: yup
          .string()
          .matches(/image/, { message: 'Only images are allowed' })
          .matches(/jpeg|png|jpg/, {
            message: 'Only jpeg, png and jpg formats are allowed'
          })
      })
    )
    .min(1, 'At least one image is required')
    .required('At least one image is required'),
  properties: yup
    .array()
    .of(
      yup.object().shape({
        id: yup.number(),
        value: yup
          .string()
          .required('This field is required')
          .matches(
            /^[a-zA-Z0-9\s]*$/,
            'Only alphanumeric characters are allowed'
          )
          .min(1, 'This field cannot be empty')
      })
    )
    .required('Properties are required')
});


export const UpdateAdvertSchema = yup.object().shape({
  title: yup
    .string()
    .min(3, 'Title must be at least 3 characters')
    .required('Title is required'),
  desc: yup
    .string()
    .min(10, 'Description must be at least 10 characters')
    .required('Description is required'),
  price: yup
    .number()
    .positive('Price must be a positive number')
    .typeError('Price must be a number')
    .required('Price is required'),
  advert_type_id: yup
    .number()
    .typeError('You have to choose an advert type')
    .required('Advert type is required'),
  category_id: yup
    .number()
    .typeError('You have to choose a category')
    .required('Category is required'),
  location: yup
    .string()
    .min(10, 'Please enter a valid address')
    .required('Address is required'),
  district_id: yup
    .number()
    .typeError('You have to choose an district')
    .required('District is required'),
  city_id: yup
    .number()
    .typeError('You have to choose an city')
    .required('City is required'),
  country_id: yup
    .number()
    .typeError('You have to choose an country')
    .required('Country is required'),
  properties: yup
    .array()
    .of(
      yup.object().shape({
        id: yup.number(),
        value: yup
          .string()
          .required('This field is required')
          .matches(
            /^[a-zA-Z0-9\s]*$/,
            'Only alphanumeric characters are allowed'
          )
          .min(1, 'This field cannot be empty')
      })
    )
    .required('Properties are required')
});
