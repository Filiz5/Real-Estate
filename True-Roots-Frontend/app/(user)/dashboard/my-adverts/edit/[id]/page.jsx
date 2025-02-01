

import { getAdvertById } from "@/services/advert-service";
import UpdateAdvert from "@/components/dashboard/update-advert/UpdateAdvert";


export default async function EditAdvertPage({ params }) {

  const response = await getAdvertById(params.id);
  const advert = response.object;
  console.log('advert in edit advert page', advert);


  return (
    <>
      <UpdateAdvert advert={advert} />
    </>
  );
}
