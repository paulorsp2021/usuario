package com.javapaulo.usuario.business;

import com.javapaulo.usuario.business.converter.UsuarioConverter;
import com.javapaulo.usuario.business.dto.EnderecoDTO;
import com.javapaulo.usuario.business.dto.TelefoneDTO;
import com.javapaulo.usuario.business.dto.UsuarioDTO;
import com.javapaulo.usuario.infrastructure.entity.Endereco;
import com.javapaulo.usuario.infrastructure.entity.Telefone;
import com.javapaulo.usuario.infrastructure.entity.Usuario;
import com.javapaulo.usuario.infrastructure.exceptions.ConflictException;
import com.javapaulo.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.javapaulo.usuario.infrastructure.repository.EnderecoRepository;
import com.javapaulo.usuario.infrastructure.repository.TelefoneRepository;
import com.javapaulo.usuario.infrastructure.repository.UsuarioRepository;
import com.javapaulo.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    
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

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){
        //busca email do token
        String email = jwtUtil.extractEmailToken(token.substring(7));
        //encripta senha
        dto.setSenha(dto.getSenha()!=null?passwordEncoder.encode(dto.getSenha()): null);
        // busca usuario do banco
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não localizado"));
        // mescla dto e banco
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);
        //salva e retorna o dto
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco(Long idEnd, EnderecoDTO enderecoDTO){

        Endereco entity = enderecoRepository.findById(idEnd).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado" + idEnd));

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO,entity);

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTel, TelefoneDTO dto){

        Telefone entity = telefoneRepository.findById(idTel).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado" + idTel));

        Telefone telefone = usuarioConverter.updateTelefone(dto, entity);

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

}
