package eu.martin.store.users;

import org.springframework.data.jpa.repository.JpaRepository;

interface AddressRepository extends JpaRepository<Address, Long> {
}