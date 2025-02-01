'use client';

import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { getAdvertTypes } from '@/services/advert-type-service';
import SearchBoxAdvertType from '@/components/admin/advert-type/advert-type-search-box';
import AdvertTypeTable from '@/components/admin/advert-type/advert-type-table';
import Spacer from '@/components/common/Spacer';
import styles from '@/styles/components/admin/advert-type/admin-advert-types.module.scss';
import { wait } from '@/utils/wait';
import Loading from '../loading';

export default function AdminAdvertTypesPage() {
  const [advertTypes, setAdvertTypes] = useState([]);
  const [filteredAdvertTypes, setFilteredAdvertTypes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const router = useRouter();

  useEffect(() => {
    const fetchAdvertTypes = async () => {
      try {
        const data = await getAdvertTypes();
        await wait(2000);
        setAdvertTypes(data?.object || []);
        setFilteredAdvertTypes(data?.object || []);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchAdvertTypes();
  }, []);

  useEffect(() => {
    const results = advertTypes.filter((type) =>
      type.title.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredAdvertTypes(results);
  }, [searchTerm, advertTypes]);

  const handleEdit = (id) => {
    router.push(`/admin-dashboard/admin-advert-types/${id}`);
  };

  const handleAddNew = () => {
    router.push('/admin-dashboard/admin-advert-types/new');
  };

  return (
    
      <div>
        <Spacer height={20} />
        <SearchBoxAdvertType 
          searchTerm={searchTerm}
          setSearchTerm={setSearchTerm}
          handleAddNew={handleAddNew}
        />
        <AdvertTypeTable
          filteredAdvertTypes={filteredAdvertTypes}
          handleEdit={handleEdit}
        />
      </div>
  );
}
