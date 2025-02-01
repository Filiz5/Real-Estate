import { getAdvertTypeById } from '@/actions/advert-type-actions';
import UpdateAdvertTypeForm from '@/components/admin/advert-type/UpdateAdvertTypeForm';

// Server Component
export default async function UpdateAdvertTypePage({ params }) {
  const { id } = params;
  const advertType = await getAdvertTypeById(id);
 await wait(2000);
  return <UpdateAdvertTypeForm initialData={advertType} id={id} />;
}
