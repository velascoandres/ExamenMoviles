from django.shortcuts import render

# Create your views here.
# Listar y crear
from rest_framework import generics

from sistemas.models import SistemaOperativo
from sistemas.serializers import SistemaOperativoSerializado


class SistemaOperativoAPI(generics.ListCreateAPIView):
    queryset = SistemaOperativo.objects.all()
    serializer_class = SistemaOperativoSerializado



#Actualizar
class SistemaOperativoUpdate(generics.UpdateAPIView):
    queryset = SistemaOperativo.objects.all()
    lookup_field = 'id'
    serializer_class = SistemaOperativoSerializado


# Borrar
class SistemaOperativoDelete(generics.DestroyAPIView):
    queryset = SistemaOperativo.objects.all()
    lookup_field = 'id'
    serializer_class = SistemaOperativoSerializado
    http_method_names = ["delete"]
    allowed_methods = ('DELETE',)