package de.perdian.flightlog.modules.registration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import de.perdian.flightlog.modules.registration.exception.RegistrationRestrictedException;
import de.perdian.flightlog.modules.registration.model.RegistrationRequest;
import de.perdian.flightlog.modules.registration.persistence.RegistrationWhitelistRepository;
import de.perdian.flightlog.modules.users.persistence.UserEntity;
import de.perdian.flightlog.modules.users.services.UsersUpdateService;

public class RegistrationServiceImplTest {

    @Test
    public void registerUserWithWhitelistPositive() {

        RegistrationConfiguration registrationConfiguration = new RegistrationConfiguration();

        RegistrationWhitelistRepository registrationWhitelistRepository = Mockito.mock(RegistrationWhitelistRepository.class);
        Mockito.when(registrationWhitelistRepository.count(Mockito.any())).thenReturn(1L);

        UserEntity usersUpdateResultEntity = new UserEntity();
        UsersUpdateService usersUpdateService = Mockito.mock(UsersUpdateService.class);
        Mockito.when(usersUpdateService.saveUser(Mockito.any())).thenReturn(usersUpdateResultEntity);

        RegistrationServiceImpl serviceImpl = new RegistrationServiceImpl();
        serviceImpl.setRegistrationConfiguration(registrationConfiguration);
        serviceImpl.setRegistrationWhitelistRepository(registrationWhitelistRepository);
        serviceImpl.setUsersUpdateService(usersUpdateService);

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setAuthenticationSource("authenticationSource");
        registrationRequest.setEmail("email@example.com");
        registrationRequest.setUsername("username");

        UserEntity registerResultEntity = serviceImpl.registerUser(registrationRequest);
        Assertions.assertEquals(usersUpdateResultEntity, registerResultEntity);

        ArgumentCaptor<UserEntity> saveUserCaptor = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(usersUpdateService).saveUser(saveUserCaptor.capture());
        Assertions.assertEquals("username", saveUserCaptor.getValue().getUsername());
        Assertions.assertEquals("authenticationSource", saveUserCaptor.getValue().getAuthenticationSource());

    }

    @Test
    public void registerUserWithWhitelistNegativeButNotRestricted() {

        RegistrationConfiguration registrationConfiguration = new RegistrationConfiguration();
        registrationConfiguration.setRestricted(false);

        RegistrationWhitelistRepository registrationWhitelistRepository = Mockito.mock(RegistrationWhitelistRepository.class);
        Mockito.when(registrationWhitelistRepository.count(Mockito.any())).thenReturn(0L);

        UserEntity usersUpdateResultEntity = new UserEntity();
        UsersUpdateService usersUpdateService = Mockito.mock(UsersUpdateService.class);
        Mockito.when(usersUpdateService.saveUser(Mockito.any())).thenReturn(usersUpdateResultEntity);

        RegistrationServiceImpl serviceImpl = new RegistrationServiceImpl();
        serviceImpl.setRegistrationConfiguration(registrationConfiguration);
        serviceImpl.setRegistrationWhitelistRepository(registrationWhitelistRepository);
        serviceImpl.setUsersUpdateService(usersUpdateService);

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setAuthenticationSource("authenticationSource");
        registrationRequest.setEmail("email@example.com");
        registrationRequest.setUsername("username");

        UserEntity registerResultEntity = serviceImpl.registerUser(registrationRequest);
        Assertions.assertEquals(usersUpdateResultEntity, registerResultEntity);

        ArgumentCaptor<UserEntity> saveUserCaptor = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(usersUpdateService).saveUser(saveUserCaptor.capture());
        Assertions.assertEquals("username", saveUserCaptor.getValue().getUsername());
        Assertions.assertEquals("authenticationSource", saveUserCaptor.getValue().getAuthenticationSource());

    }

    @Test
    public void registerUserWithWhitelistNegative() {

        RegistrationConfiguration registrationConfiguration = new RegistrationConfiguration();
        registrationConfiguration.setRestricted(true);

        RegistrationWhitelistRepository registrationWhitelistRepository = Mockito.mock(RegistrationWhitelistRepository.class);
        Mockito.when(registrationWhitelistRepository.count(Mockito.any())).thenReturn(0L);

        UsersUpdateService usersUpdateService = Mockito.mock(UsersUpdateService.class);

        RegistrationServiceImpl serviceImpl = new RegistrationServiceImpl();
        serviceImpl.setRegistrationConfiguration(registrationConfiguration);
        serviceImpl.setRegistrationWhitelistRepository(registrationWhitelistRepository);
        serviceImpl.setUsersUpdateService(usersUpdateService);

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setAuthenticationSource("authenticationSource");
        registrationRequest.setEmail("email@example.com");
        registrationRequest.setUsername("username");

        Assertions.assertThrows(RegistrationRestrictedException.class, () -> serviceImpl.registerUser(registrationRequest));
        Mockito.verifyNoMoreInteractions(usersUpdateService);

    }

}
