import { auth } from '@/auth';
import { getAdvertBySlug } from '@/services/getAdvertBySlug-service';
import { AdvertDetails } from '@/components/adverts/advertDetails';

export const metadata = {
  title: 'PROPERTY DETAILS',
  description: 'Check out your dream property'
};

export default async function Slug({ params }) {
  const session = await auth();
 
  const res = await getAdvertBySlug(params.slug);
let ownAdvert = false;
  if (session?.user?.object?.userId === res?.object?.userId) {
    ownAdvert = true;
  }

 

  return (
    <>
      <AdvertDetails res={res} ownAdvert={ownAdvert} session={session} />
    </>
  );
}
