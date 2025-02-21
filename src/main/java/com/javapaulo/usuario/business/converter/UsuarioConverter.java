package com.javapaulo.usuario.business.converter;

import com.javapaulo.usuario.business.dto.EnderecoDTO;
import com.javapaulo.usuario.business.dto.TelefoneDTO;
import com.javapaulo.usuario.business.dto.UsuarioDTO;
import com.javapaulo.usuario.infrastructure.entity.Endereco;
import com.javapaulo.usuario.infrastructure.entity.Telefone;
import com.javapaulo.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario paraUsuario(UsuarioDTO usuarioDTO){
       return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(usuarioDTO.getEnderecos() != null ?
                    paraListaEndereco(usuarioDTO.getEnderecos()) : null)
                .telefones(usuarioDTO.getTelefones() != null ?
                    paraListaTelefones(usuarioDTO.getTelefones()) : null)
                .build();
    }

    private List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecosDTOS) {
        return enderecosDTOS.stream().map(this::paraEndereco).toList();
        //               OU
        // List<Endereco> enderecos = new ArrayList<>();
        //for(EnderecoDTO enderecoDTO : enderecoDTOS){
        //    enderecos.add(paraEndereco(enderecoDTO));
        //}
        //return enderecos;
    }

    public Endereco paraEndereco(EnderecoDTO enderecoDTO){
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .cidade(enderecoDTO.getCidade())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .estado(enderecoDTO.getEstado())
                .build();
    }

    public List<Telefone> paraListaTelefones(List<TelefoneDTO> telefoneDTOS){
        return telefoneDTOS.stream().map(this::paraTelefone).toList();
    }

    public Telefone paraTelefone(TelefoneDTO telefoneDTO){
        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

    public UsuarioDTO paraUsuarioDTO(Usuario usuario){
        return UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .enderecos(usuario.getEnderecos() != null ?
                        paraListaEnderecoDTO(usuario.getEnderecos()) : null)
                .telefones(usuario.getTelefones() != null ?
                        paraListaTelefonesDTO(usuario.getTelefones()) : null)
                .build();
    }


    private List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> enderecos) {
        return enderecos.stream().map(this::paraEnderecoDTO).toList();
        //               OU
        // List<Endereco> enderecos = new ArrayList<>();
        //for(EnderecoDTO enderecoDTO : enderecoDTOS){
        //    enderecos.add(paraEndereco(enderecoDTO));
        //}
        //return enderecos;
    }

    public EnderecoDTO paraEnderecoDTO(Endereco endereco){
        return EnderecoDTO.builder()
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .cidade(endereco.getCidade())
                .complemento(endereco.getComplemento())
                .cep(endereco.getCep())
                .estado(endereco.getEstado())
                .build();
    }

    public List<TelefoneDTO> paraListaTelefonesDTO(List<Telefone> telefones){
        return telefones.stream().map(this::paraTelefoneDTO).toList();
    }

    public TelefoneDTO paraTelefoneDTO(Telefone telefones){
        return TelefoneDTO.builder()
                .numero(telefones.getNumero())
                .ddd(telefones.getDdd())
                .build();
    }

    public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario entity){
        return Usuario.builder()
                .nome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : entity.getNome())
                .id(entity.getId())
                .senha(usuarioDTO.getSenha()!= null ? usuarioDTO.getSenha() : entity.getSenha())
                .email(usuarioDTO.getEmail()!=null?usuarioDTO.getEmail():entity.getEmail())
                .enderecos(entity.getEnderecos())
                .telefones(entity.getTelefones())
                .build();
    }
}
