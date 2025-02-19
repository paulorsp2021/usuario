package com.javapaulo.usuario.business;

import com.javapaulo.usuario.business.converter.UsuarioConverter;
import com.javapaulo.usuario.business.dto.UsuarioDTO;
import com.javapaulo.usuario.infrastructure.entity.Usuario;
import com.javapaulo.usuario.infrastructure.exceptions.ConflictException;
import com.javapaulo.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.javapaulo.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    
    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExiste((usuarioDTO.getEmail()));
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado " + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado ", e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {

        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email)
                            .orElseThrow(
                                    () -> new ResourceNotFoundException("Email não encontrado " + email)
                            )
            );
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email não encontrado " + email);
        }
    }


    public void deletaUsuarioPorEmail(String email){

        usuarioRepository.deleteByEmail(email);
    }

}
