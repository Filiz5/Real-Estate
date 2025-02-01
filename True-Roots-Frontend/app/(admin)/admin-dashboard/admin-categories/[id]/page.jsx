import UpdateCategory from '@/components/admin/category/UpdateCategory'
import { getCategoryById } from '@/services/category-service';
import { wait } from '@/utils/wait';

export default async function UpdateCategories({ params }) {
  const data = await getCategoryById(params.id); // Kategori ID'ye göre veriyi çek
  await wait(2000);
  return <UpdateCategory data={data} />;
}