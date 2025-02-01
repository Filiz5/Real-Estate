/**
 * Filter and sort data by title
 * This function filters the data by the role and sorts the rest of the items by title.
 * @param {array | undefined} data
 * @param {string} role
 * @returns array
 */

export const filterAndSortDataByTitle= (data, role) => {
    if(!data || !role) return [];

    const filteredData = data.filter((item) => item.roles.includes(role));

    const restOfItems = filteredData.slice(1).sort((a,b) => a.title.localeCompare(b.title));

    return [filteredData[0], ...restOfItems];
}